package RMIForum.RMICore;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TopicList {
    private CopyOnWriteArrayList<TopicClass> topicList;

    public TopicList(){
        topicList = new CopyOnWriteArrayList<>();
    }

    /**
     *
     * @return list of registered topics.
     */
    public List<String> ListTopicName(){
        List<String> res = new ArrayList<>();
        for (TopicClass t : topicList){
            res.add(t.getName());
        }
        return res;
    }

    /**
     *
     * @param topic: named of the searched topic
     * @return
     */
    public boolean contains(String topic){
        return ListTopicName().contains(topic);
    }

    public List<MessageClass> getConversation(String topicname){
        return getTopicNamed(topicname).getMessagesAsMessage();
    }

    public TopicClass getTopicNamed(String topicName) throws NoSuchElementException{
        if(!contains(topicName)) throw new NoSuchElementException();
        for(TopicClass t : topicList) if(t.getName().equals(topicName)) return t;
        return null;
    }

    public T put(TopicClass tc){
        return topicList.add(tc);
    }
}