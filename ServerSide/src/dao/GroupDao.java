package dao;

import models.Group;
import models.GroupMessage;
import models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface GroupDao {
    Group getGroup(String username) throws Exception;
    void addGroup(Group group);
    void deleteGroup(String username);
    void updateGroup(Group group);
    ArrayList<String> getAllGroup() throws Exception;
    Set<User> getGroupUsers(String username) throws Exception;
    List<GroupMessage> getGroupMessage(String username) throws Exception;
}
