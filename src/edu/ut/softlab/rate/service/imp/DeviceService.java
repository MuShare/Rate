package edu.ut.softlab.rate.service.imp;


import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.dao.IDeviceDao;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Device;
import edu.ut.softlab.rate.model.User;
import edu.ut.softlab.rate.service.IDeviceService;
import edu.ut.softlab.rate.service.common.AbstractService;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 16-8-3.
 */

@RemoteProxy
@Service("deviceService")
public class DeviceService extends AbstractService<Device> implements IDeviceService{
    @Resource(name="deviceDao")
    private IDeviceDao deviceDao;

    @Override
    public User findUserByToken(String token) {
        return deviceDao.findUserByToken(token);
    }

    @Override
    public String updateToken(String currentToken, String deviceToken, String ip, String lan) {
        Device device = deviceDao.findDeviceByToken(currentToken);
        if(device == null){
            return null;
        }else {
            device.setDeviceToken(deviceToken);
            device.setLastLoginTime(new Date());
            String token = Utility.getToken(currentToken);
            device.setLoginToken(token);
            device.setLastLoginIp(ip);
            device.setLan(lan);
            deviceDao.update(device);
            return token;
        }
    }

    @Override
    public List<Device> findDeviceByDeviceId(String deviceId) {
        return deviceDao.findDeviceByDeviceId(deviceId);
    }

    @Override
    protected IOperations<Device> getDao() {
        return deviceDao;
    }
}
