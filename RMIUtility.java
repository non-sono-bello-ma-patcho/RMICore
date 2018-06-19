package RMIForum.RMICore;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.security.AccessControlException;
/* why won't you update? */
public class RMIUtility {
    private Registry ServerRegistry;
    private int serverPort;
    private int clientPort;
    private String Salias;
    private  String Calias;

    public RMIUtility (int sp, int cp, String sa, String ca){
        clientPort = cp;
        serverPort = sp;
        Salias = sa;
        Calias = ca;
    }

    public int serverSetUp(Remote obj, String Localhost) {
        System.setProperty("java.rmi.server.hostname", Localhost);
        System.setProperty("java.security.policy", System.getProperty("os.name").equals("Linux")?"/tmp/RMIServer.policy":"C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Local\\Temp\\RMIServer.policy"); // define directory for windows...        if (System.getSecurityManager()==null) System.setSecurityManager(new SecurityManager());
        int res = -1;
        // RMIServer obj = new RMIServer();
        try {
            ServerRegistry=setRegistry(serverPort);
            res = ExportNBind(ServerRegistry, obj, Salias,serverPort);
            System.err.println((obj instanceof RMIServerInterface?"Server up and running on:"+Localhost:"Registry correctly set")); /* non va bene per il client*/
        } catch (AccessControlException e) {
            System.err.println("You must set policy in order to set registry!");
            // showStackTrace(e);
            System.exit(1);
        } catch (RemoteException e) {
            System.err.println("Couldn't set registry, maybe you want to check stack trace?[S/n]");
            showStackTrace(e);
        } catch (AlreadyBoundException e) {
            System.err.println("Couldn't export and bind, maybe you want to check stack trace?[S/n]");
            showStackTrace(e);
        }
        return res;
    }

    static void showStackTrace(Exception e){
        /*Scanner sc = new Scanner(System.in);
        if(sc.nextInt()!='n')*/ e.printStackTrace();
    }

    public Remote getRemoteMethod(String host) throws RemoteException, NotBoundException {
        System.err.println("Trying to retrieve registry from"+host+"...");
        Registry registry = LocateRegistry.getRegistry(host, clientPort);
        System.err.print("LookingUp for share Object: ");
        return registry.lookup(Calias);
    }

    public void RMIshutDown(Remote obj) throws RemoteException, NotBoundException {
        ServerRegistry.unbind(Salias);
        UnicastRemoteObject.unexportObject(obj, true);
    }

    private Registry setRegistry(int port) throws RemoteException {
        try {
            return LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            return LocateRegistry.getRegistry(port);
        }
    }

    private int ExportNBind(Registry reg, Remote obj, String alias, int port) throws AlreadyBoundException, RemoteException {
        int res = port;        
        try {
            Remote stub = UnicastRemoteObject.exportObject(obj, port);
            reg.bind(alias, stub);
        }
        catch (ExportException e){
            return ExportNBind(reg, obj, alias, port++);
        }
        return res;
    }
}
