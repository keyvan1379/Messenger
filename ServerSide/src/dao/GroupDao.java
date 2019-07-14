package dao;

import models.Group;
import models.GroupMessage;
import models.User;

import java.util.ArrayList;
import java.util.Set;

public interface GroupDao {
    Group getGroup(String username) throws Exception;
    void addGroup(Group group);
    void deleteGroup(String username);
    void updateGroup(Group group);
    Set<User> getGroupUsers(String username) throws Exception;
    ArrayList<GroupMessage> getGroupMessage(String username) throws Exception;
}
