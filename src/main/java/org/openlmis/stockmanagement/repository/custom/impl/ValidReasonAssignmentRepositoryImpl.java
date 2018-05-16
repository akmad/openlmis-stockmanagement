/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.stockmanagement.repository.custom.impl;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.openlmis.stockmanagement.domain.reason.ReasonType;
import org.openlmis.stockmanagement.domain.reason.StockCardLineItemReason;
import org.openlmis.stockmanagement.domain.reason.ValidReasonAssignment;
import org.openlmis.stockmanagement.repository.custom.ValidReasonAssignmentRepositoryCustom;

public class ValidReasonAssignmentRepositoryImpl implements ValidReasonAssignmentRepositoryCustom {

  private static final String PROGRAM_ID = "programId";
  private static final String FACILITY_TYPE_ID = "facilityTypeId";
  private static final String ID = "id";
  private static final String REASON_TYPE = "reasonType";
  private static final String REASON = "reason";

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * This method is supposed to retrieve all Processing Periods with matched parameters.
   * Method is searching
   *
   * @param programId  Processing Schedule associated to Processing Period(
   * @param facilityTypeId Processing Period Start Date
   * @param reasonTypes   Processing Period End Date
   * @param reasonId  pagination and sorting parameters
   * @return List of Processing Periods matching the parameters.
   */
  public List<ValidReasonAssignment> search(UUID programId, UUID facilityTypeId,
      Collection<ReasonType> reasonTypes, UUID reasonId) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<ValidReasonAssignment> query = builder.createQuery(ValidReasonAssignment.class);

    Root<ValidReasonAssignment> root = query.from(ValidReasonAssignment.class);

    Predicate predicate = builder.conjunction();

    if (null != programId) {
      predicate = builder.and(predicate, builder.equal(root.get(PROGRAM_ID), programId));
    }

    if (null != facilityTypeId) {
      predicate = builder.and(predicate, builder.equal(root.get(FACILITY_TYPE_ID), facilityTypeId));
    }

    if (null != reasonId) {
      Join<ValidReasonAssignment, StockCardLineItemReason> stockReason = root.join(
          REASON, JoinType.LEFT);
      predicate = builder.and(predicate, builder.equal(stockReason.get(ID), reasonId));
    }

    if (null != reasonTypes) {
      Join<ValidReasonAssignment, StockCardLineItemReason> stockReason = root.join(
          REASON, JoinType.LEFT);
      predicate = builder.and(predicate, stockReason.get(REASON_TYPE).in(reasonTypes));
    }

    query.where(predicate);

    return entityManager.createQuery(query)
        .getResultList();
  }
}
