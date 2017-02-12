package org.hermesrtc.signalingserver.repository;

import com.google.common.collect.Maps;

import org.hermesrtc.signalingserver.Names;
import org.hermesrtc.signalingserver.api.HermesRTCEventBus;
import org.hermesrtc.signalingserver.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.websocket.Session;

import static org.hermesrtc.signalingserver.api.HermesRTCEvents.*;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
public class Members {

    @Autowired
    @Qualifier(Names.EVENT_BUS)
    private HermesRTCEventBus eventBus;

    private Map<String, Member> members = Maps.newConcurrentMap();

    public Collection<String> getAllIds() {
        return members.keySet();
    }

    public Optional<Member> findBy(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(members.get(id));
    }

    public void register(Member member) {
        members.computeIfAbsent(member.getId(), put -> member);
        eventBus.post(SESSION_OPENED.occurFor(member.getSession()));
    }

    public void unregisterBy(Session session, String reason) {
        unregister(session.getId());
        eventBus.post(SESSION_CLOSED.occurFor(session, reason));
    }

    private void unregister(String id) {
        findBy(id).ifPresent(Member::markLeft);
        Member removed = members.remove(id);
        if (removed != null) {
            removed.getConversation().ifPresent(c -> c.left(removed));
        }
    }

    public void dropOutAfterException(Session session, String reason) {
        unregister(session.getId());
        eventBus.post(UNEXPECTED_SITUATION.occurFor(session, reason));
    }
}
