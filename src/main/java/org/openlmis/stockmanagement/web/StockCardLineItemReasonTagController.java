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

package org.openlmis.stockmanagement.web;

import java.util.List;
import org.openlmis.stockmanagement.repository.StockCardLineItemReasonRepository;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/stockCardLineItemReasonTags")
public class StockCardLineItemReasonTagController extends BaseController {

  @Autowired
  private StockCardLineItemReasonRepository reasonRepository;

  /**
   * Retrieve all stock card line item reason tags.
   */
  @GetMapping
  @ResponseBody
  public List<String> getAllReasonTags() {
    Profiler profiler = getProfiler("GET_REASON_TAGS");

    profiler.start("DB_CALL");
    List<String> tags = reasonRepository.findTags();

    return stopProfiler(profiler, tags);
  }

}
