package edu.ut.softlab.rate.service;

import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Device;
import edu.ut.softlab.rate.model.User;

/**
 * Created by alex on 16-8-3.
 */
public interface IDeviceService extends IOperations<Device>{
    User findUserByToken(String token);
}
