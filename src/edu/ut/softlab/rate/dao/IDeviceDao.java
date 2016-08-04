package edu.ut.softlab.rate.dao;

import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Device;
import edu.ut.softlab.rate.model.User;

/**
 * Created by alex on 16-8-3.
 */
public interface IDeviceDao extends IOperations<Device>{
    User findUserByToken(String token);
}