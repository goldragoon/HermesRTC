package org.hermesrtc.signalingserver.repository;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hermesrtc.signalingserver.Names;
import org.hermesrtc.signalingserver.api.HermesRTCEndpoint;
import org.hermesrtc.signalingserver.api.HermesRTCEventBus;
import org.hermesrtc.signalingserver.domain.Conversation;
import org.hermesrtc.signalingserver.domain.InternalMessage;
import org.hermesrtc.signalingserver.domain.Member;
import org.hermesrtc.signalingserver.domain.conversation.BroadcastConversation;
import org.hermesrtc.signalingserver.domain.conversation.MeshConversation;
import org.hermesrtc.videochat.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.hermesrtc.signalingserver.api.HermesRTCEvents.CONVERSATION_CREATED;
import static org.hermesrtc.signalingserver.api.HermesRTCEvents.CONVERSATION_DESTROYED;
import static org.hermesrtc.signalingserver.domain.EventContext.builder;
import static org.hermesrtc.signalingserver.exception.Exceptions.CONVERSATION_NAME_OCCUPIED;
import static org.hermesrtc.signalingserver.exception.Exceptions.INVALID_CONVERSATION_NAME;

@Repository
public class Conversations {
	
	private static final Logger log = Logger.getLogger(HermesRTCEndpoint.class);
    @Autowired
    @Qualifier(Names.EVENT_BUS)
    private HermesRTCEventBus eventBus;

    @Autowired
    private ApplicationContext context;

    private Map<String, Conversation> conversations = Maps.newConcurrentMap();
    private Map<String, ArrayList<String>> conversationsPerDeveloper = Maps.newConcurrentMap();
    
    public Optional<Conversation> findBy(String id) {
        if (isEmpty(id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(conversations.get(id));
    }

    public ArrayList<String> findByAppKey(String appKey) {
        return conversationsPerDeveloper.get(appKey);
    }
    
    public void remove(String id, Member sender) {
        eventBus.post(CONVERSATION_DESTROYED.basedOn(
                builder()
                        .conversation(conversations.remove(id))
                        .from(sender)));
    }

    public Conversation create(InternalMessage message) {
    	
    	// legacy code for older work
        //String conversationName = getConversationName(message.getContent());
    	
    	// changed code : conversation name is generated randomly.    	
    	String conversationName = new KeyGenerator().nextSessionId();
    	log.info("NEW Conversation created " + conversationName);
    	if(conversationsPerDeveloper.get(message.getContent()) == null)
    	{
    		ArrayList<String> conversationIds = new ArrayList<String>();
    		conversationsPerDeveloper.put(message.getContent(), conversationIds);
    	}
    	conversationsPerDeveloper.get(message.getContent()).add(conversationName);
    	
        final Conversation conversation = create(conversationName, Optional.ofNullable(message.getCustom().get("type")));
        postEvent(message, conversation);
        return conversation;
    }

    private void postEvent(InternalMessage message, Conversation conversation) {
        eventBus.post(CONVERSATION_CREATED.basedOn(message, conversation));
    }

    private Conversation create(String conversationName, Optional<String> optionalType) {
        String type = optionalType.orElse("MESH");
        Conversation conversation = null;
        if (type.equalsIgnoreCase("BROADCAST")) {
            conversation = context.getBean(BroadcastConversation.class, conversationName);
        } else if (type.equalsIgnoreCase("MESH")) {
            conversation = context.getBean(MeshConversation.class, conversationName);
        }
        registerInContainer(conversation);
        return conversation;
    }

    private void registerInContainer(Conversation conversation) {
        conversations.put(conversation.getId(), conversation);
    }

    private void validate(String name) {
        if (isEmpty(name)) {
            throw INVALID_CONVERSATION_NAME.exception();
        }
        if (conversations.containsKey(name)) {
            throw CONVERSATION_NAME_OCCUPIED.exception();
        }
    }

    public Collection<String> getAllIds() {
        return conversations.keySet();
    }
}
