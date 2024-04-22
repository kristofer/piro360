import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './piro.reducer';

export const PiroDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const piroEntity = useAppSelector(state => state.piro.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="piroDetailsHeading">Piro</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{piroEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{piroEntity.title}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{piroEntity.description}</dd>
          <dt>
            <span id="s3urltovideo">S 3 Urltovideo</span>
          </dt>
          <dd>{piroEntity.s3urltovideo}</dd>
          <dt>
            <span id="created">Created</span>
          </dt>
          <dd>{piroEntity.created ? <TextFormat value={piroEntity.created} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>Owner</dt>
          <dd>{piroEntity.owner ? piroEntity.owner.id : ''}</dd>
          <dt>Tags</dt>
          <dd>
            {piroEntity.tags
              ? piroEntity.tags.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {piroEntity.tags && i === piroEntity.tags.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/piro" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/piro/${piroEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PiroDetail;
