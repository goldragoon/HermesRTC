package org.hermesrtc.signalingserver.domain;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hermesrtc.signalingserver.api.HermesRTCEndpoint;
import org.hermesrtc.signalingserver.api.dto.HermesRTCConversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Member;
import org.hermesrtc.signalingserver.domain.Signal;
import org.hermesrtc.signalingserver.repository.Conversations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

@Component
@Scope("prototype")
public abstract class Conversation implements HermesRTCConversation {

    protected final String id;
    protected String appKey;
    private static final Logger log = Logger.getLogger(HermesRTCEndpoint.class);
    @Autowired
    private Conversations conversations;
    protected Member master;
    protected Set<Member> modifyAuthorizedMembers = Sets.newConcurrentHashSet();
    
    public Conversation(String id) {
        this.id = id;
    }

    public abstract void join(Member sender);
    public abstract void delegate(Member sender, Member receiver);
    public abstract void grantModification(Member sender, Member receiver);
    public abstract void depriveModification(Member sender, Member receiver);
    
    public boolean isMemberModifiable(Member member)
    {
    	return member != null && (modifyAuthorizedMembers.contains(member) || master.equals(member));
    }
    
    public void left(Member sender) {
        if (remove(sender)) {
            if (isWithoutMember()) {
                //unregisterConversation(sender, this);
            }
        }
    }

    protected abstract boolean remove(Member leaving);

    protected void assignSenderToConversation(Member sender) {
        sender.assign(this);
    }
    public Member getMaster(){return master;}
    
    public void setMaster(Member member)
    {
    	if(has(member))
    	{
    		this.master = member;
    		log.info("Master of Conversation [" + id +"] changed to " + member.getId());
    	}
    	else
    	{
    		
    	}
    }
    
    public abstract boolean isWithoutMember();

    public abstract boolean has(Member from);

    private void unregisterConversation(Member sender, Conversation conversation) {
        conversations.remove(conversation.getId(), sender);
    }

    public String getId() {
        return this.id;
    }

    public abstract void exchangeSignals(InternalMessage message);
    
    protected void sendJoinedToConversation(Member sender, String id) {
        InternalMessage.create()//
                .to(sender)//
                .content(id)//
                .signal(Signal.JOIN)//
                .build()//
                .send();
    }

    protected void sendJoinedFrom(Member sender, Member member) {
        InternalMessage.create()//
                .from(sender)//
                .to(member)//
                .signal(Signal.NEW_JOINED)//
                .content(sender.getId())
                .build()//
                .send();
    }

    protected void sendLeftMessage(Member leaving, Member recipient) {
        InternalMessage.create()//
                .from(leaving)//
                .to(recipient)//
                .signal(Signal.LEFT)//
                .build()//
                .send();
    }
    public void sendDelegatedTo(Member delegater, Member delegatee) {
        InternalMessage.create()//
                .from(delegater)//
                .to(delegatee)//
                .signal(Signal.DELEGATED)//
                .build()//
                .send();
    }
    public abstract void broadcast(Member from, InternalMessage message);
}
