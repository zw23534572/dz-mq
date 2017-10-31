import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.junit.Test;

public class ZookeeperTest {

    @Test
    public void test7() throws InterruptedException{
        ZkClient zkClient = new ZkClient("172.16.21.21:2181", 1000, 1000, new SerializableSerializer());
        if(!zkClient.exists("/yanghui/data")){
            zkClient.createPersistent("/yanghui/data");
        }
        if(!zkClient.exists("/yanghui/md5")){
            zkClient.createPersistent("/yanghui/md5");
        }
        if(!zkClient.exists("/yanghui/providers")){
            zkClient.createPersistent("/yanghui/providers");
        }
        if(!zkClient.exists("/yanghui/providers/172.16.21.24:9012")){
            zkClient.createEphemeral("/yanghui/providers/172.16.21.24:9012");
            zkClient.create("/yanghui/providers/172.16.21.24:9012", "1", ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void test8() throws InterruptedException{
        ZkClient zkClient = new ZkClient("172.16.21.21:2181", 1000, 1000, new SerializableSerializer());
        zkClient.delete("/yanghui/providers/172.16.21.24:9012",-1);
        zkClient.delete("/yanghui/data",-1);
        zkClient.delete("/yanghui/md5",-1);
    }
}
