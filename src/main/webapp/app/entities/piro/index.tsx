import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Piro from './piro';
import PiroDetail from './piro-detail';
import PiroUpdate from './piro-update';
import PiroDeleteDialog from './piro-delete-dialog';

const PiroRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Piro />} />
    <Route path="new" element={<PiroUpdate />} />
    <Route path=":id">
      <Route index element={<PiroDetail />} />
      <Route path="edit" element={<PiroUpdate />} />
      <Route path="delete" element={<PiroDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PiroRoutes;
