package com.mycompany.dashboard.system.web.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.dashboard.service.CommonConditionQueryService;
import com.mycompany.dashboard.system.domain.SiteConfig;
import com.mycompany.dashboard.system.repository.SiteConfigRepository;
import com.mycompany.dashboard.system.service.SiteConfigQueryService;
import com.mycompany.dashboard.system.service.SiteConfigService;
import com.mycompany.dashboard.system.service.criteria.SiteConfigCriteria;
import com.mycompany.dashboard.system.service.dto.SiteConfigDTO;
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

 * ????????????{@link com.mycompany.dashboard.system.domain.SiteConfig}???REST Controller???
 */
@RestController
@RequestMapping("/api")
@Api(value = "site-configs", tags = "????????????API??????")
public class SiteConfigResource {

    private final Logger log = LoggerFactory.getLogger(SiteConfigResource.class);

    private static final String ENTITY_NAME = "siteConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteConfigService siteConfigService;

    private final SiteConfigRepository siteConfigRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final SiteConfigQueryService siteConfigQueryService;

    public SiteConfigResource(
        SiteConfigService siteConfigService,
        SiteConfigRepository siteConfigRepository,
        CommonConditionQueryService commonConditionQueryService,
        SiteConfigQueryService siteConfigQueryService
    ) {
        this.siteConfigService = siteConfigService;
        this.siteConfigRepository = siteConfigRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.siteConfigQueryService = siteConfigQueryService;
    }

    /**
     * {@code POST  /site-configs} : Create a new siteConfig.
     *
     * @param siteConfigDTO the siteConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new siteConfigDTO, or with status {@code 400 (Bad Request)} if the siteConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/site-configs")
    @ApiOperation(value = "??????????????????", notes = "???????????????????????????????????????")
    public ResponseEntity<SiteConfigDTO> createSiteConfig(@RequestBody SiteConfigDTO siteConfigDTO) throws URISyntaxException {
        log.debug("REST request to save SiteConfig : {}", siteConfigDTO);
        if (siteConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new siteConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SiteConfigDTO result = siteConfigService.save(siteConfigDTO);
        return ResponseEntity
            .created(new URI("/api/site-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /site-configs/:id} : Updates an existing siteConfig.
     *
     * @param id the id of the siteConfigDTO to save.
     * @param siteConfigDTO the siteConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteConfigDTO,
     * or with status {@code 400 (Bad Request)} if the siteConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/site-configs/{id}")
    @ApiOperation(value = "??????????????????", notes = "?????????????????????????????????????????????????????????")
    public ResponseEntity<SiteConfigDTO> updateSiteConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SiteConfigDTO siteConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SiteConfig : {}, {}", id, siteConfigDTO);
        if (siteConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteConfigService.exists(SiteConfig::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SiteConfigDTO result = siteConfigService.save(siteConfigDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, siteConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /site-configs/:id} : Partial updates given fields of an existing siteConfig, field will ignore if it is null.
     *
     * @param id the id of the siteConfigDTO to save.
     * @param siteConfigDTO the siteConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteConfigDTO,
     * or with status {@code 400 (Bad Request)} if the siteConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the siteConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the siteConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "????????????????????????", notes = "??????????????????????????????????????????????????????null????????????????????????????????????????????????????????????")
    @PatchMapping(value = "/site-configs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SiteConfigDTO> partialUpdateSiteConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SiteConfigDTO siteConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SiteConfig partially : {}, {}", id, siteConfigDTO);
        if (siteConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (siteConfigRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SiteConfigDTO> result = siteConfigService.partialUpdate(siteConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, siteConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /site-configs} : get all the siteConfigs.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of siteConfigs in body.
     */
    @GetMapping("/site-configs")
    @ApiOperation(value = "??????????????????????????????", notes = "???????????????????????????????????????")
    public ResponseEntity<List<SiteConfigDTO>> getAllSiteConfigs(
        SiteConfigCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get SiteConfigs by criteria: {}", criteria);
        IPage<SiteConfigDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = siteConfigQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = siteConfigQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = siteConfigQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = siteConfigQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /site-configs/count} : count all the siteConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/site-configs/count")
    public ResponseEntity<Long> countSiteConfigs(SiteConfigCriteria criteria) {
        log.debug("REST request to count SiteConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(siteConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /site-configs/:id} : get the "id" siteConfig.
     *
     * @param id the id of the siteConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siteConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/site-configs/{id}")
    @ApiOperation(value = "?????????????????????????????????", notes = "???????????????????????????????????????")
    public ResponseEntity<SiteConfigDTO> getSiteConfig(@PathVariable Long id) {
        log.debug("REST request to get SiteConfig : {}", id);
        Optional<SiteConfigDTO> siteConfigDTO = siteConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(siteConfigDTO);
    }

    /**
     * GET  /site-configs/export : export the siteConfigs.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the siteConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/site-configs/export")
    @ApiOperation(value = "????????????EXCEL??????", notes = "???????????????????????????EXCEL??????")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<SiteConfigDTO> page = siteConfigService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("?????????????????????", "??????"), SiteConfigDTO.class, page.getRecords());
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
     * POST  /site-configs/import : import the siteConfigs from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the siteConfigDTO, or with status 404 (Not Found)
     */
    @PostMapping("/site-configs/import")
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
        List<SiteConfigDTO> list = ExcelImportUtil.importExcel(savedFile, SiteConfigDTO.class, params);
        list.forEach(siteConfigService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /site-configs/:id} : delete the "id" siteConfig.
     *
     * @param id the id of the siteConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/site-configs/{id}")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????????????????")
    public ResponseEntity<Void> deleteSiteConfig(@PathVariable Long id) {
        log.debug("REST request to delete SiteConfig : {}", id);
        siteConfigService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /site-configs} : delete all the "ids" SiteConfigs.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/site-configs")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????????????????")
    public ResponseEntity<Void> deleteSiteConfigsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SiteConfigs : {}", ids);
        if (ids != null) {
            ids.forEach(siteConfigService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /site-configs/specified-fields} : Updates an existing siteConfig by specified fields.
     *
     * @param siteConfigDTOAndSpecifiedFields the siteConfigDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteConfigDTO,
     * or with status {@code 400 (Bad Request)} if the siteConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/site-configs/specified-fields")
    @ApiOperation(value = "????????????????????????????????????", notes = "??????????????????????????????????????????????????????????????????????????????????????????null")
    public ResponseEntity<SiteConfigDTO> updateSiteConfigBySpecifiedFields(
        @RequestBody SiteConfigDTOAndSpecifiedFields siteConfigDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update SiteConfig : {}", siteConfigDTOAndSpecifiedFields);
        SiteConfigDTO siteConfigDTO = siteConfigDTOAndSpecifiedFields.getSiteConfig();
        Set<String> specifiedFields = siteConfigDTOAndSpecifiedFields.getSpecifiedFields();
        if (siteConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SiteConfigDTO result = siteConfigService.updateBySpecifiedFields(siteConfigDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /site-configs/specified-field} : Updates an existing siteConfig by specified field.
     *
     * @param siteConfigDTOAndSpecifiedFields the siteConfigDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteConfigDTO,
     * or with status {@code 400 (Bad Request)} if the siteConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/site-configs/specified-field")
    @ApiOperation(value = "??????????????????????????????", notes = "????????????????????????????????????????????????????????????????????????????????????null")
    public ResponseEntity<SiteConfigDTO> updateSiteConfigBySpecifiedField(
        @RequestBody SiteConfigDTOAndSpecifiedFields siteConfigDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update SiteConfig : {}", siteConfigDTOAndSpecifiedFields);
        SiteConfigDTO siteConfigDTO = siteConfigDTOAndSpecifiedFields.getSiteConfig();
        String fieldName = siteConfigDTOAndSpecifiedFields.getSpecifiedField();
        if (siteConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SiteConfigDTO result = siteConfigService.updateBySpecifiedField(siteConfigDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class SiteConfigDTOAndSpecifiedFields {

        private SiteConfigDTO siteConfig;
        private Set<String> specifiedFields;
        private String specifiedField;

        private SiteConfigDTO getSiteConfig() {
            return siteConfig;
        }

        private void setSiteConfig(SiteConfigDTO siteConfig) {
            this.siteConfig = siteConfig;
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
