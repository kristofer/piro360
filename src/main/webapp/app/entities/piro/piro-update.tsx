import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ITag } from 'app/shared/model/tag.model';
import { getEntities as getTags } from 'app/entities/tag/tag.reducer';
import { IPiro } from 'app/shared/model/piro.model';
import { getEntity, updateEntity, createEntity, reset } from './piro.reducer';

export const PiroUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const tags = useAppSelector(state => state.tag.entities);
  const piroEntity = useAppSelector(state => state.piro.entity);
  const loading = useAppSelector(state => state.piro.loading);
  const updating = useAppSelector(state => state.piro.updating);
  const updateSuccess = useAppSelector(state => state.piro.updateSuccess);

  const handleClose = () => {
    navigate('/piro');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getTags({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...piroEntity,
      ...values,
      owner: users.find(it => it.id.toString() === values.owner?.toString()),
      tags: mapIdList(values.tags),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...piroEntity,
          owner: piroEntity?.owner?.id,
          tags: piroEntity?.tags?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="piro360App.piro.home.createOrEditLabel" data-cy="PiroCreateUpdateHeading">
            Create or edit a Piro
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="piro-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Title"
                id="piro-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="piro-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="S 3 Urltovideo" id="piro-s3urltovideo" name="s3urltovideo" data-cy="s3urltovideo" type="text" />
              <ValidatedField label="Created" id="piro-created" name="created" data-cy="created" type="date" />
              <ValidatedField id="piro-owner" name="owner" data-cy="owner" label="Owner" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Tags" id="piro-tags" data-cy="tags" type="select" multiple name="tags">
                <option value="" key="0" />
                {tags
                  ? tags.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/piro" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PiroUpdate;
