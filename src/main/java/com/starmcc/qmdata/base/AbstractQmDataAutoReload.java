package com.starmcc.qmdata.base;

import java.util.List;

/**
 * @author qm
 * @Title QmBase实现类
 * @Date 2019/1/23 12:43
 */
public abstract class AbstractQmDataAutoReload extends AbstractQmDataAutoBase {

    @Override
    public <Q> List<Q> autoSelectList(Q entity, Class<Q> clamm) {
        return super.autoSelectList(entity, null, null, clamm);
    }

    @Override
    public <Q> List<Q> autoSelectList(Q entity) {
        return super.autoSelectList(entity, null, null, null);
    }

    @Override
    public <Q> List<Q> autoSelectList(Class<Q> clamm) {
        return super.autoSelectList(null, null, null, clamm);
    }

    @Override
    public <Q> Q autoSelectOne(Q entity) {
        return super.autoSelectOne(entity, null, null, null);
    }

    @Override
    public <Q> Q autoSelectOne(Class<Q> clamm) {
        return super.autoSelectOne(null, null, null, clamm);
    }

    @Override
    public <Q> List<Q> autoSelectList(Q entity, String where) {
        return super.autoSelectList(entity, where, null, null);
    }

    @Override
    public <Q> List<Q> autoSelectList(Class<Q> clamm, String where) {
        return super.autoSelectList(null, where, null, clamm);
    }

    @Override
    public <Q> Q autoSelectOne(Q entity, String where) {
        return super.autoSelectOne(entity, where, null, null);
    }

    @Override
    public <Q> Q autoSelectOne(Class<Q> clamm, String where) {
        return super.autoSelectOne(null, where, null, clamm);
    }

    @Override
    public <Q> List<Q> autoSelectList(Q entity, String where, String orderBy) {
        return super.autoSelectList(entity, where, orderBy, null);
    }

    @Override
    public <Q> Q autoSelectOne(Q entity, String where, String orderBy) {
        return super.autoSelectOne(entity, where, orderBy, null);
    }

    @Override
    public <Q> Q autoSelectOne(Q entity, Class<Q> clamm) {
        return super.autoSelectOne(entity, null, null, clamm);
    }

    @Override
    public <Q> List<Q> autoSelectList(Q entity, String where, Class<Q> clamm) {
        return super.autoSelectList(entity, where, null, clamm);
    }

    @Override
    public <Q> Q autoSelectOne(Q entity, String where, Class<Q> clamm) {
        return super.autoSelectOne(entity, where, null, clamm);
    }

    @Override
    public <Q> int autoUpdate(Q entity) {
        return super.autoUpdate(entity, null);
    }

    @Override
    public <Q> int autoDelete(Q entity) {
        return super.autoDelete(entity, null);
    }

    @Override
    public <Q> int autoSelectCount(Q entity) {
        return super.autoSelectCount(entity, null);
    }

}
