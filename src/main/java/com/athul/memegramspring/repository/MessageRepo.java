package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepo extends MongoRepository<Message,String> {

    Optional<List<Message>> findByChatIdOrderByTimeDesc(String roomId);

}
