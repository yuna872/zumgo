package com.isf6.backend.config.handler;

import com.isf6.backend.domain.repository.ChatRepository;
import com.isf6.backend.service.ChatService;
import com.isf6.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final UserService userService;
    private final ChatRepository chatRepository;
    private final ChatService chatService;

    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청, 로그인 여부 확인
            String jwtToken = accessor.getFirstNativeHeader("AUTH");
            log.info("CONNECT {}", jwtToken);
            // Header의 jwt token 검증
//            jwtTokenProvider.validateToken(jwtToken);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청, user가 일치하는지 확인하고 일치한다면 채팅방 목록에 추가함.
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            chatRepository.setUserEnterInfo(sessionId, roomId);
            // 클라이언트 입장
            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("사용자 ");
            log.info("SUBSCRIBED {}, {}", name, roomId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRepository.getUserEnterRoomId(sessionId);
            log.info("DISCONNECTED {}, {}", sessionId, roomId);
        }
        return message;
    }
}
