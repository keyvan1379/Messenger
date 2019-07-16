package dao;

import models.Channel;
import models.ChannelMessage;
import models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface ChannelDao {
    Channel getChannel(String username) throws Exception;
    void addChannel( Channel channel);
    void deleteChannel(String username);
    void updateChannel( Channel channel);
    ArrayList<String> getAllChannel() throws Exception;
    ArrayList<Channel> getAllChannels() throws Exception;
    Set<User> getChannelUsers(String username) throws Exception;
    List<ChannelMessage> getChannelMessage(String username) throws Exception;
}
