package com.starmcc.qmdata.base;

import java.util.List;

/**
 * @author starmcc
 * @version 2019/1/23 12:43
 * QmBase多层实现
 */
public abstract class AbstractQmDataAutoReload extends AbstractQmDataAutoBase {


    @Override
    public <Q, M> List<M> autoSelectList(Q entity, String where, Class<M> clamm) {
        return super.autoSelectList(entity, where, null, clamm);
    }

    @Override
    public <Q, M> List<M> autoSelectList(Q entity, Class<M> clamm) {
        return super.autoSelectList(entity, null, null, clamm);
    }

    @Override
    public <M> List<M> autoSelectList(String where, Class<M> clamm) {
        return super.autoSelectList(null, where, null, clamm);
    }


    @Override
    public <Q, M> M autoSelectOne(Q entity, String where, Class<M> clamm) {
        return super.autoSelectOne(entity, where, null, clamm);
    }

    @Override
    public <M> M autoSelectOne(String where, Class<M> clamm) {
        return super.autoSelectOne(null, where, null, clamm);
    }

    @Override
    public <Q, M> M autoSelectOne(Q entity, Class<M> clamm) {
        return super.autoSelectOne(entity, null, null, clamm);
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
    public <Q, M> Long autoSelectCount(Q entity, Class<M> clamm) {
        return super.autoSelectCount(entity, null, clamm);
    }

    @Override
    public <M> List<M> autoSelectList(M entity) {
        return super.autoSelectList(entity, null, null, null);
    }

    @Override
    public <M> M autoSelectOne(M entity) {
        return super.autoSelectOne(entity, null, null, null);
    }

    @Override
    public <M> Long autoSelectCount(M entity) {
        return super.autoSelectCount(entity, null, null);
    }

    @Override
    public <M> Long autoSelectCount(M entity, String whereSql) {
        return super.autoSelectCount(entity, whereSql, null);
    }

    @Override
    public <M> List<M> autoSelectList(M entity, String where, String orderBy) {
        return super.autoSelectList(entity, where, orderBy, null);
    }

    @Override
    public <M> List<M> autoSelectList(M entity, String where) {
        return super.autoSelectList(entity, where, null, null);
    }

    @Override
    public <M> M autoSelectOne(M entity, String where, String orderBy) {
        return super.autoSelectOne(entity, where, orderBy, null);
    }

    @Override
    public <M> M autoSelectOne(M entity, String where) {
        return super.autoSelectOne(entity, where, null, null);
    }
}
