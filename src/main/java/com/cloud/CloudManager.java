package com.cloud;

import com.constant.Constant;
import com.dao.InformationDao;
import com.entity.Information;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/2/15
 */
public class CloudManager {

    private static Logger logger = LoggerFactory.getLogger(CloudManager.class);

    private String connectString = "";

    private static InformationDao informationDao;

    public CloudManager(String connectString,InformationDao informationDao){

        this.connectString = connectString;
        this.informationDao = informationDao;
    }
    /**
     * 用于标示是哪个工程的znode节点
     */
    public static final String ZK_PERJECT_NODE = "/txsvr";//7.2通讯服务器

    private static CountDownLatch latch = new CountDownLatch(1);

    private static Optional<ZooKeeper> zooKeeper = Optional.empty();

    private void updateStatus(String id,int status){
        Information information = informationDao.findByCode(id);
        information.setStatus(status);
        informationDao.save(information);
    }


    /**
     * create zookeeper session And get zookeeper object
     * @return
     * @throws Exception
     */
    private Optional<ZooKeeper> getZooKeeper() throws Exception{

        if(!zooKeeper.isPresent()){
            zooKeeper = Optional.of(new ZooKeeper(connectString,15000
                    ,(event) ->{
                            if(event.getState() == Watcher.Event.KeeperState.SyncConnected){
                                latch.countDown();
                            }
            }));
        }
        return zooKeeper;
    }

    /**
     * create projectZNode on zookeeper
     * @throws Exception
     */
    public void createProjectPersistNode(){
        try{
            getZooKeeper().get().create(ZK_PERJECT_NODE
                    ,ZK_PERJECT_NODE.getBytes(Charset.forName("UTF-8"))
                    , ZooDefs.Ids.OPEN_ACL_UNSAFE
                    , CreateMode.PERSISTENT);
        }catch (KeeperException.ConnectionLossException ex){
            createProjectPersistNode();
        }catch(KeeperException.NodeExistsException ex){
            logger.info("project ZNode {} exists",ZK_PERJECT_NODE);
        }catch(Exception ex){
            logger.error("project ZNode create failed,{}",ex.getMessage());
        }
    }

    public void exist(String childrenCode){
        Stat stat  = new Stat();

        try {
            getZooKeeper().get().exists(childrenCode
                    ,(event) -> {
                        if(Watcher.Event.KeeperState.SyncConnected == event.getState()){

                            if(Watcher.Event.EventType.NodeDeleted == event.getType()){
                                updateStatus(childrenCode.split("/")[2], 0);
                                logger.info("node not exist svr not run");
                                exist(childrenCode);
                            }
                            if(Watcher.Event.EventType.NodeCreated == event.getType()){
                                updateStatus(childrenCode.split("/")[2],1);
                                logger.info("node exist svr running");
                                exist(childrenCode);
                            }
                        }
                    }
                    ,existCallback
                    ,stat);
        } catch (Exception e) {
            logger.error("exist function error:{}",e.getMessage());
        }
    }

    private AsyncCallback.StatCallback existCallback = (rc, path, ctx, name) -> {
        switch(KeeperException.Code.get(rc)){
            case CONNECTIONLOSS:
                exist(path);
                break;
            case OK:
                break;
            case NONODE:
                break;
            default:
                break;
        }
    };




}
