package com.globant.projectdynamodb.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.globant.projectdynamodb.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    private DynamoDBMapper mapper;

    public void insertIntoDynamoDB(User user){
        mapper.save(user);
    }

    public User getUser(String userId, String email){
        return mapper.load(User.class, userId, email);
    }

    public void updateUser(User user){
        try{
            //mapper.save(user, buildDynamoDBSaveExpression(user));
            User entity = getUser(user.getId(), user.getEmail());
            if(entity == null){
                entity = new User();
            }
            entity.setName(user.getName());
            entity.setSurname(user.getSurname());
            entity.setEmail(user.getEmail());
            mapper.save(user);
        }catch (ConditionalCheckFailedException except){
            LOGGER.error("invalid data - " + except.getMessage());
        }
    }

    public void deleteUser(User user){
        mapper.delete(user);
    }

    /**
     * Checks if the user already exists
     * @param user
     * @return
     */
    public DynamoDBSaveExpression buildDynamoDBSaveExpression(User user){
        DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expected = new HashMap<>();
        expected.put("id", new ExpectedAttributeValue(new AttributeValue(user.getId())))
                .withComparisonOperator(ComparisonOperator.EQ);
        saveExpression.setExpected(expected);
        return saveExpression;
    }
}
