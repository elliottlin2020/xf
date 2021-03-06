package com.mycompany.dashboard.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.dashboard.domain.SysFillRule;
import com.mycompany.dashboard.repository.SysFillRuleRepository;
import com.mycompany.dashboard.service.CommonConditionQueryService;
import com.mycompany.dashboard.service.SysFillRuleQueryService;
import com.mycompany.dashboard.service.SysFillRuleService;
import com.mycompany.dashboard.service.criteria.SysFillRuleCriteria;
import com.mycompany.dashboard.service.dto.SysFillRuleDTO;
import com.mycompany.dashboard.util.web.IPageUtil;
import com.mycompany.dashboard.util.web.PageableUtils;
import com.mycompany.dashboard.web.rest.errors.BadRequestAlertException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * ????????????{@link com.mycompany.dashboard.domain.SysFillRule}???REST Controller???
 */
@RestController
@RequestMapping("/api")
@Api(value = "sys-fill-rules", tags = "????????????API??????")
public class SysFillRuleResource {

    private final Logger log = LoggerFactory.getLogger(SysFillRuleResource.class);

    private static final String ENTITY_NAME = "settingsSysFillRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysFillRuleService sysFillRuleService;

    private final SysFillRuleRepository sysFillRuleRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final SysFillRuleQueryService sysFillRuleQueryService;

    public SysFillRuleResource(
        SysFillRuleService sysFillRuleService,
        SysFillRuleRepository sysFillRuleRepository,
        CommonConditionQueryService commonConditionQueryService,
        SysFillRuleQueryService sysFillRuleQueryService
    ) {
        this.sysFillRuleService = sysFillRuleService;
        this.sysFillRuleRepository = sysFillRuleRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.sysFillRuleQueryService = sysFillRuleQueryService;
    }

    /**
     * {@code POST  /sys-fill-rules} : Create a new sysFillRule.
     *
     * @param sysFillRuleDTO the sysFillRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysFillRuleDTO, or with status {@code 400 (Bad Request)} if the sysFillRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sys-fill-rules")
    @ApiOperation(value = "??????????????????", notes = "???????????????????????????????????????")
    public ResponseEntity<SysFillRuleDTO> createSysFillRule(@RequestBody SysFillRuleDTO sysFillRuleDTO) throws URISyntaxException {
        log.debug("REST request to save SysFillRule : {}", sysFillRuleDTO);
        if (sysFillRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new sysFillRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysFillRuleDTO result = sysFillRuleService.save(sysFillRuleDTO);
        return ResponseEntity
            .created(new URI("/api/sys-fill-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-fill-rules/:id} : Updates an existing sysFillRule.
     *
     * @param id the id of the sysFillRuleDTO to save.
     * @param sysFillRuleDTO the sysFillRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysFillRuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysFillRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysFillRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-fill-rules/{id}")
    @ApiOperation(value = "??????????????????", notes = "?????????????????????????????????????????????????????????")
    public ResponseEntity<SysFillRuleDTO> updateSysFillRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysFillRuleDTO sysFillRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SysFillRule : {}, {}", id, sysFillRuleDTO);
        if (sysFillRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysFillRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysFillRuleService.exists(SysFillRule::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SysFillRuleDTO result = sysFillRuleService.save(sysFillRuleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sysFillRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sys-fill-rules/:id} : Partial updates given fields of an existing sysFillRule, field will ignore if it is null.
     *
     * @param id the id of the sysFillRuleDTO to save.
     * @param sysFillRuleDTO the sysFillRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysFillRuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysFillRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sysFillRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sysFillRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "????????????????????????", notes = "??????????????????????????????????????????????????????null????????????????????????????????????????????????????????????")
    @PatchMapping(value = "/sys-fill-rules/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SysFillRuleDTO> partialUpdateSysFillRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysFillRuleDTO sysFillRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SysFillRule partially : {}, {}", id, sysFillRuleDTO);
        if (sysFillRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysFillRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (sysFillRuleRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SysFillRuleDTO> result = sysFillRuleService.partialUpdate(sysFillRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sysFillRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sys-fill-rules} : get all the sysFillRules.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sysFillRules in body.
     */
    @GetMapping("/sys-fill-rules")
    @ApiOperation(value = "??????????????????????????????", notes = "???????????????????????????????????????")
    public ResponseEntity<List<SysFillRuleDTO>> getAllSysFillRules(
        SysFillRuleCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get SysFillRules by criteria: {}", criteria);
        IPage<SysFillRuleDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = sysFillRuleQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = sysFillRuleQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = sysFillRuleQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = sysFillRuleQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /sys-fill-rules/count} : count all the sysFillRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sys-fill-rules/count")
    public ResponseEntity<Long> countSysFillRules(SysFillRuleCriteria criteria) {
        log.debug("REST request to count SysFillRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(sysFillRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sys-fill-rules/:id} : get the "id" sysFillRule.
     *
     * @param id the id of the sysFillRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sysFillRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sys-fill-rules/{id}")
    @ApiOperation(value = "?????????????????????????????????", notes = "???????????????????????????????????????")
    public ResponseEntity<SysFillRuleDTO> getSysFillRule(@PathVariable Long id) {
        log.debug("REST request to get SysFillRule : {}", id);
        Optional<SysFillRuleDTO> sysFillRuleDTO = sysFillRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sysFillRuleDTO);
    }

    /**
     * GET  /sys-fill-rules/export : export the sysFillRules.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the sysFillRuleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sys-fill-rules/export")
    @ApiOperation(value = "????????????EXCEL??????", notes = "???????????????????????????EXCEL??????")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<SysFillRuleDTO> page = sysFillRuleService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("?????????????????????", "??????"),
            SysFillRuleDTO.class,
            page.getRecords()
        );
        File savefile = new File("export");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("export/personDTO_2018_09_10.xls");
        workbook.write(fos);
        fos.close();
        return ResponseEntity.ok().build();
    }

    /**
     * POST  /sys-fill-rules/import : import the sysFillRules from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the sysFillRuleDTO, or with status 404 (Not Found)
     */
    @PostMapping("/sys-fill-rules/import")
    @ApiOperation(value = "????????????EXCEL??????", notes = "??????????????????EXCEL????????????????????????")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws IOException {
        String fileRealName = file.getOriginalFilename(); //?????????????????????;
        int pointIndex = fileRealName.lastIndexOf("."); //???????????????
        String fileSuffix = fileRealName.substring(pointIndex); //??????????????????
        String fileNewName = UUID.randomUUID().toString(); //??????new???????????????
        String saveFileName = fileNewName.concat(fileSuffix); //???????????????
        String filePath = "import";
        File path = new File(filePath); //??????????????????????????????????????????????????????????????????
        if (!path.exists()) {
            path.mkdirs();
        }
        File savedFile = new File(filePath, saveFileName);
        file.transferTo(savedFile);
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<SysFillRuleDTO> list = ExcelImportUtil.importExcel(savedFile, SysFillRuleDTO.class, params);
        list.forEach(sysFillRuleService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /sys-fill-rules/:id} : delete the "id" sysFillRule.
     *
     * @param id the id of the sysFillRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sys-fill-rules/{id}")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????????????????")
    public ResponseEntity<Void> deleteSysFillRule(@PathVariable Long id) {
        log.debug("REST request to delete SysFillRule : {}", id);
        sysFillRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /sys-fill-rules} : delete all the "ids" SysFillRules.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sys-fill-rules")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????????????????")
    public ResponseEntity<Void> deleteSysFillRulesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SysFillRules : {}", ids);
        if (ids != null) {
            ids.forEach(sysFillRuleService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /sys-fill-rules/specified-fields} : Updates an existing sysFillRule by specified fields.
     *
     * @param sysFillRuleDTOAndSpecifiedFields the sysFillRuleDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysFillRuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysFillRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysFillRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-fill-rules/specified-fields")
    @ApiOperation(value = "????????????????????????????????????", notes = "??????????????????????????????????????????????????????????????????????????????????????????null")
    public ResponseEntity<SysFillRuleDTO> updateSysFillRuleBySpecifiedFields(
        @RequestBody SysFillRuleDTOAndSpecifiedFields sysFillRuleDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update SysFillRule : {}", sysFillRuleDTOAndSpecifiedFields);
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleDTOAndSpecifiedFields.getSysFillRule();
        Set<String> specifiedFields = sysFillRuleDTOAndSpecifiedFields.getSpecifiedFields();
        if (sysFillRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SysFillRuleDTO result = sysFillRuleService.updateBySpecifiedFields(sysFillRuleDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysFillRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-fill-rules/specified-field} : Updates an existing sysFillRule by specified field.
     *
     * @param sysFillRuleDTOAndSpecifiedFields the sysFillRuleDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysFillRuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysFillRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysFillRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-fill-rules/specified-field")
    @ApiOperation(value = "??????????????????????????????", notes = "????????????????????????????????????????????????????????????????????????????????????null")
    public ResponseEntity<SysFillRuleDTO> updateSysFillRuleBySpecifiedField(
        @RequestBody SysFillRuleDTOAndSpecifiedFields sysFillRuleDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update SysFillRule : {}", sysFillRuleDTOAndSpecifiedFields);
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleDTOAndSpecifiedFields.getSysFillRule();
        String fieldName = sysFillRuleDTOAndSpecifiedFields.getSpecifiedField();
        if (sysFillRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SysFillRuleDTO result = sysFillRuleService.updateBySpecifiedField(sysFillRuleDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class SysFillRuleDTOAndSpecifiedFields {

        private SysFillRuleDTO sysFillRule;
        private Set<String> specifiedFields;
        private String specifiedField;

        private SysFillRuleDTO getSysFillRule() {
            return sysFillRule;
        }

        private void setSysFillRule(SysFillRuleDTO sysFillRule) {
            this.sysFillRule = sysFillRule;
        }

        private Set<String> getSpecifiedFields() {
            return specifiedFields;
        }

        private void setSpecifiedFields(Set<String> specifiedFields) {
            this.specifiedFields = specifiedFields;
        }

        public String getSpecifiedField() {
            return specifiedField;
        }

        public void setSpecifiedField(String specifiedField) {
            this.specifiedField = specifiedField;
        }
    }
}
