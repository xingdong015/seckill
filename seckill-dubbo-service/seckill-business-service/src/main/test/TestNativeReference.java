import com.system.design.seckill.business.service.KillBuzService;
import com.system.design.seckill.common.api.IAccountService;
import com.system.design.seckill.common.api.IKillBuzService;
import com.system.design.seckill.common.po.Account;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class TestNativeReference {
    public static void main(String[] args) throws InterruptedException, IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startWithExport();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runWithRefer();
            }
        }).start();
        System.in.read();

    }
    private static void runWithRefer() {
        ReferenceConfig<IKillBuzService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("seckill-business-provider"));
        reference.setRegistry(new RegistryConfig("nacos://rgb-fk.com:8848"));
        reference.setInterface(IKillBuzService.class);
        IKillBuzService service = reference.get();
        Map<String, Object> map = service.getById("1");
        System.out.println(map);
    }

    private static void startWithExport() throws InterruptedException {
        ServiceConfig<IKillBuzService> service = new ServiceConfig<>();
        service.setInterface(IKillBuzService.class);
        service.setRef(new KillBuzService());

        ApplicationConfig applicationConfig = new ApplicationConfig("seckill-business-server");
        applicationConfig.setQosEnable(false);
        applicationConfig.setCompiler("jdk");

        Map<String, String> m = new HashMap<>(1);
        m.put("proxy", "jdk");
        applicationConfig.setParameters(m);

        service.setApplication(applicationConfig);
        service.setRegistry(new RegistryConfig("nacos://rgb-fk.com:8848"));
        service.export();

        System.out.println("dubbo service started");
        new CountDownLatch(1).await();
    }
}