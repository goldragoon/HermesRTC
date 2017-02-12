package org.hermesrtc.signalingserver.domain.conversation;

import com.google.common.collect.Sets;

import org.apache.log4j.Logger;
import org.hermesrtc.signalingserver.Names;
import org.hermesrtc.signalingserver.api.HermesRTCEndpoint;
import org.hermesrtc.signalingserver.api.HermesRTCEventBus;
import org.hermesrtc.signalingserver.cases.ExchangeSignalsBetweenMembers;
import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Member;
import org.hermesrtc.signalingserver.domain.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.hermesrtc.signalingserver.exception.Exceptions.UNAUTHORIZED_ACCESS;

import java.util.Map;
import java.util.Set;

@Component
@Scope("prototype")
public class MeshConversation extends Conversation {
    @Autowired
    private ExchangeSignalsBetweenMembers exchange;
    private static final Logger log = Logger.getLogger(HermesRTCEndpoint.class);
    @Autowired
    @Qualifier(Names.EVENT_BUS)
    private HermesRTCEventBus eventBus;

    private Set<Member> members = Sets.newConcurrentHashSet();

    public MeshConversation(String id) {
        super(id);
    }
    
    public synchronized void delegate(Member delegater, Member delegatee) {
		for(Member to : members)
		{
			sendDelegatedTo(delegatee, to);
			log.info("Send delegated to " + to.getId());
		}
		/*
		if(getMaster().equals(delegater))
		{

		}
		else
		{
			throw UNAUTHORIZED_ACCESS.exception();
		}*/
    }
    
    @Override
    public synchronized void join(Member sender) {
        assignSenderToConversation(sender);

        informSenderThatHasBeenJoined(sender);

        informRestAndBeginSignalExchange(sender);

        members.add(sender);
    }

    private void informRestAndBeginSignalExchange(Member sender) {
        for (Member to : members) {
            sendJoinedFrom(sender, to);
            exchange.begin(to, sender, (m1, m2) -> true);
        }
    }

    private void informSenderThatHasBeenJoined(Member sender) {
        if (isWithoutMember()) {
            sendJoinedToFirst(sender, id);
        } else {
            sendJoinedToConversation(sender, id);
        }
    }

    public synchronized boolean isWithoutMember() {
        return members.isEmpty();
    }

    public synchronized boolean has(Member member) {
        return member != null && members.contains(member);
    }

    @Override
    public void exchangeSignals(InternalMessage message) {
        exchange.execute(message);
    }

    @Override
    public void broadcast(Member from, InternalMessage message) {
        members.stream()
                .filter(member -> !member.equals(from))
                .forEach(to -> message.copy()
                        .from(from)
                        .to(to)
                        .build()
                        .send());
    }

    @Override
    public synchronized boolean remove(Member leaving) {
        boolean remove = members.remove(leaving);
        if (remove) {
            leaving.unassignConversation(this);
            for (Member member : members) {
                sendLeftMessage(leaving, member);
            }
        }
        return remove;
    }

    private void sendJoinedToFirst(Member sender, String id) {
        InternalMessage.create()//
                .to(sender)//
                .signal(Signal.CREATE)//
                .content(id)//
                .build()//
                .send();
    }
    
	@Override
	public void grantModification(Member sender, Member receiver) {
		 
		//if(!isMemberModifiable(receiver))
		//{
			modifyAuthorizedMembers.add(receiver);
		//}	
		/*
		if(master.equals(sender))
		{

		}
		else
		{
			throw UNAUTHORIZED_ACCESS.exception();
		}*/
			for(Member to : members)
			{
		        InternalMessage.create()//
                .to(to)//
                .signal(Signal.ENMODIFIED)//
                .content(receiver.getId())//
                .build()//
                .send();
				log.info("Send enmodified to " + to.getId());
			}
	}

	@Override
	public void depriveModification(Member sender, Member receiver) {
		modifyAuthorizedMembers.remove(receiver);
		/*
		if(master.equals(sender))
		{
			if(isMemberModifiable(receiver))
			{
				modifyAuthorizedMembers.remove(receiver);
			}	
		}
		else
		{
			throw UNAUTHORIZED_ACCESS.exception();
		}*/
		
		for(Member to : members)
		{
	        InternalMessage.create()//
            .to(to)//
            .signal(Signal.DEMODIFIED)//
            .content(receiver.getId())//
            .build()//
            .send();
			log.info("Send demodified to " + to.getId());
		}
	}
	

}
