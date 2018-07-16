package RMIForum.RMICore;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIClient extends Remote {
    void CLiNotify(String TopicLabel, String TriggeredBy, boolean type) throws RemoteException;
    List<String> getMyTopic() throws RemoteException;
}
