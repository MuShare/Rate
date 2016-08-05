package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.dao.IDeviceDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.Device;
import edu.ut.softlab.rate.model.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created by alex on 16-8-3.
 */

@Repository("deviceDao")
public class DeviceDao extends AbstractHibernateDao<Device> implements IDeviceDao{
    public DeviceDao(){
        super();
        setClass(Device.class);
    }

    @Override
    public User findUserByToken(String token) {
        Criteria criteria = getCurrentSesstion().createCriteria(Device.class)
                .add(Restrictions.eq("loginToken", token));
        Device device = (Device)criteria.uniqueResult();
        if(device == null){
            return null;
        }else {
            return device.getUser();
        }
    }

    @Override
    public Device findDeviceByToken(String token) {
        Criteria criteria = getCurrentSesstion().createCriteria(Device.class)
                .add(Restrictions.eq("loginToken", token));
        Device device = (Device)criteria.uniqueResult();
        if(device == null){
            return null;
        }else {
            return device;
        }
    }

    @Override
    public Device findDeviceByDeviceId(String deviceId) {
        Criteria criteria = getCurrentSesstion().createCriteria(Device.class)
                .add(Restrictions.eq("deviceId", deviceId));
        Device device = (Device)criteria.uniqueResult();
        if(device == null){
            return null;
        }else {
            return device;
        }
    }
}
