package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepo extends MongoRepository<ChatRoom,String> {

    ChatRoom findByParticipantUserIds(List<Integer> participantUserIds);

    @Query("{'participantUserIds': ?0, $or: [{'messages': null}, {'messages': {$exists: false}}, {'messages': {$size: 0}}, {'messages': {$not: {$size: 0}} }]}")
    ChatRoom findByParticipantUserIdsWithMessages(List<Integer> participantUserIds);


}
