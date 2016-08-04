package edu.ut.softlab.rate.service.imp;


import edu.ut.softlab.rate.dao.IDeviceDao;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Device;
import edu.ut.softlab.rate.model.User;
import edu.ut.softlab.rate.service.IDeviceService;
import edu.ut.softlab.rate.service.common.AbstractService;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    protected IOperations<Device> getDao() {
        return deviceDao;
    }
}
