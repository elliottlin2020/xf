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
import com.mycompany.dashboard.domain.RegionCode;
import com.mycompany.dashboard.repository.RegionCodeRepository;
import com.mycompany.dashboard.service.CommonConditionQueryService;
import com.mycompany.dashboard.service.RegionCodeQueryService;
import com.mycompany.dashboard.service.RegionCodeService;
import com.mycompany.dashboard.service.criteria.RegionCodeCriteria;
import com.mycompany.dashboard.service.dto.RegionCodeDTO;
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

 * 管理实体{@link com.mycompany.dashboard.domain.RegionCode}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "region-codes", tags = "行政区划码API接口")
public class RegionCodeResource {

    private final Logger log = LoggerFactory.getLogger(RegionCodeResource.class);

    private static final String ENTITY_NAME = "settingsRegionCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegionCodeService regionCodeService;

    private final RegionCodeRepository regionCodeRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final RegionCodeQueryService regionCodeQueryService;

    public RegionCodeResource(
        RegionCodeService regionCodeService,
        RegionCodeRepository regionCodeRepository,
        CommonConditionQueryService commonConditionQueryService,
        RegionCodeQueryService regionCodeQueryService
    ) {
        this.regionCodeService = regionCodeService;
        this.regionCodeRepository = regionCodeRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.regionCodeQueryService = regionCodeQueryService;
    }

    /**
     * {@code POST  /region-codes} : Create a new regionCode.
     *
     * @param regionCodeDTO the regionCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new regionCodeDTO, or with status {@code 400 (Bad Request)} if the regionCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/region-codes")
    @ApiOperation(value = "新建行政区划码", notes = "创建并返回一个新的行政区划码")
    public ResponseEntity<RegionCodeDTO> createRegionCode(@RequestBody RegionCodeDTO regionCodeDTO) throws URISyntaxException {
        log.debug("REST request to save RegionCode : {}", regionCodeDTO);
        if (regionCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new regionCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegionCodeDTO result = regionCodeService.save(regionCodeDTO);
        return ResponseEntity
            .created(new URI("/api/region-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /region-codes/:id} : Updates an existing regionCode.
     *
     * @param id the id of the regionCodeDTO to save.
     * @param regionCodeDTO the regionCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regionCodeDTO,
     * or with status {@code 400 (Bad Request)} if the regionCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the regionCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/region-codes/{id}")
    @ApiOperation(value = "更新行政区划码", notes = "根据主键更新并返回一个更新后的行政区划码")
    public ResponseEntity<RegionCodeDTO> updateRegionCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RegionCodeDTO regionCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RegionCode : {}, {}", id, regionCodeDTO);
        if (regionCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, regionCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!regionCodeService.exists(RegionCode::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RegionCodeDTO result = regionCodeService.save(regionCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, regionCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /region-codes/:id} : Partial updates given fields of an existing regionCode, field will ignore if it is null.
     *
     * @param id the id of the regionCodeDTO to save.
     * @param regionCodeDTO the regionCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regionCodeDTO,
     * or with status {@code 400 (Bad Request)} if the regionCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the regionCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the regionCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(
        value = "部分更新行政区划码",
        notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的行政区划码"
    )
    @PatchMapping(value = "/region-codes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RegionCodeDTO> partialUpdateRegionCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RegionCodeDTO regionCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RegionCode partially : {}, {}", id, regionCodeDTO);
        if (regionCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, regionCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (regionCodeRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RegionCodeDTO> result = regionCodeService.partialUpdate(regionCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, regionCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /region-codes} : get all the regionCodes.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of regionCodes in body.
     */
    @GetMapping("/region-codes")
    @ApiOperation(value = "获取行政区划码分页列表", notes = "获取行政区划码的分页列表数据")
    public ResponseEntity<List<RegionCodeDTO>> getAllRegionCodes(
        RegionCodeCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get RegionCodes by criteria: {}", criteria);
        IPage<RegionCodeDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = regionCodeQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = regionCodeQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = regionCodeQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = regionCodeQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /region-codes/count} : count all the regionCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/region-codes/count")
    public ResponseEntity<Long> countRegionCodes(RegionCodeCriteria criteria) {
        log.debug("REST request to count RegionCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(regionCodeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /region-codes/tree : get all the regionCodes for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of regionCodes in body
     */
    @GetMapping("/region-codes/tree")
    @ApiOperation(value = "获取行政区划码树形列表", notes = "获取行政区划码的树形列表数据")
    public ResponseEntity<List<RegionCodeDTO>> getAllRegionCodesofTree(Pageable pageable) {
        log.debug("REST request to get a page of RegionCodes");
        IPage<RegionCodeDTO> page = regionCodeService.findAllTop(PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * GET  /region-codes/{parentId}/tree : get all the regionCodes for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of regionCodes in body
     */
    @GetMapping("/region-codes/{parentId}/tree")
    @ApiOperation(value = "获取行政区划码指定节点下的树形列表", notes = "获取行政区划码指定节点下的树形列表数据")
    public ResponseEntity<List<RegionCodeDTO>> getAllRegionCodesofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all RegionCodes of parentId");
        List<RegionCodeDTO> list = regionCodeService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /region-codes/:id} : get the "id" regionCode.
     *
     * @param id the id of the regionCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the regionCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/region-codes/{id}")
    @ApiOperation(value = "获取指定主键的行政区划码", notes = "获取指定主键的行政区划码信息")
    public ResponseEntity<RegionCodeDTO> getRegionCode(@PathVariable Long id) {
        log.debug("REST request to get RegionCode : {}", id);
        Optional<RegionCodeDTO> regionCodeDTO = regionCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(regionCodeDTO);
    }

    /**
     * GET  /region-codes/export : export the regionCodes.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the regionCodeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/region-codes/export")
    @ApiOperation(value = "行政区划码EXCEL导出", notes = "导出全部行政区划码为EXCEL文件")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<RegionCodeDTO> page = regionCodeService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("计算机一班学生", "学生"), RegionCodeDTO.class, page.getRecords());
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
     * POST  /region-codes/import : import the regionCodes from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the regionCodeDTO, or with status 404 (Not Found)
     */
    @PostMapping("/region-codes/import")
    @ApiOperation(value = "行政区划码EXCEL导入", notes = "根据行政区划码EXCEL文件导入全部数据")
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws IOException {
        String fileRealName = file.getOriginalFilename(); //获得原始文件名;
        int pointIndex = fileRealName.lastIndexOf("."); //点号的位置
        String fileSuffix = fileRealName.substring(pointIndex); //截取文件后缀
        String fileNewName = UUID.randomUUID().toString(); //文件new名称时间戳
        String saveFileName = fileNewName.concat(fileSuffix); //文件存取名
        String filePath = "import";
        File path = new File(filePath); //判断文件路径下的文件夹是否存在，不存在则创建
        if (!path.exists()) {
            path.mkdirs();
        }
        File savedFile = new File(filePath, saveFileName);
        file.transferTo(savedFile);
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<RegionCodeDTO> list = ExcelImportUtil.importExcel(savedFile, RegionCodeDTO.class, params);
        list.forEach(regionCodeService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /region-codes/:id} : delete the "id" regionCode.
     *
     * @param id the id of the regionCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/region-codes/{id}")
    @ApiOperation(value = "删除一个行政区划码", notes = "根据主键删除单个行政区划码")
    public ResponseEntity<Void> deleteRegionCode(@PathVariable Long id) {
        log.debug("REST request to delete RegionCode : {}", id);
        regionCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /region-codes} : delete all the "ids" RegionCodes.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/region-codes")
    @ApiOperation(value = "删除多个行政区划码", notes = "根据主键删除多个行政区划码")
    public ResponseEntity<Void> deleteRegionCodesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete RegionCodes : {}", ids);
        if (ids != null) {
            ids.forEach(regionCodeService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /region-codes/specified-fields} : Updates an existing regionCode by specified fields.
     *
     * @param regionCodeDTOAndSpecifiedFields the regionCodeDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regionCodeDTO,
     * or with status {@code 400 (Bad Request)} if the regionCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the regionCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/region-codes/specified-fields")
    @ApiOperation(value = "根据字段部分更新行政区划码", notes = "根据指定字段部分更新行政区划码，给定的属性值可以为任何值，包括null")
    public ResponseEntity<RegionCodeDTO> updateRegionCodeBySpecifiedFields(
        @RequestBody RegionCodeDTOAndSpecifiedFields regionCodeDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update RegionCode : {}", regionCodeDTOAndSpecifiedFields);
        RegionCodeDTO regionCodeDTO = regionCodeDTOAndSpecifiedFields.getRegionCode();
        Set<String> specifiedFields = regionCodeDTOAndSpecifiedFields.getSpecifiedFields();
        if (regionCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegionCodeDTO result = regionCodeService.updateBySpecifiedFields(regionCodeDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, regionCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /region-codes/specified-field} : Updates an existing regionCode by specified field.
     *
     * @param regionCodeDTOAndSpecifiedFields the regionCodeDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regionCodeDTO,
     * or with status {@code 400 (Bad Request)} if the regionCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the regionCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/region-codes/specified-field")
    @ApiOperation(value = "更新行政区划码单个属性", notes = "根据指定字段更新行政区划码，给定的属性值可以为任何值，包括null")
    public ResponseEntity<RegionCodeDTO> updateRegionCodeBySpecifiedField(
        @RequestBody RegionCodeDTOAndSpecifiedFields regionCodeDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update RegionCode : {}", regionCodeDTOAndSpecifiedFields);
        RegionCodeDTO regionCodeDTO = regionCodeDTOAndSpecifiedFields.getRegionCode();
        String fieldName = regionCodeDTOAndSpecifiedFields.getSpecifiedField();
        if (regionCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegionCodeDTO result = regionCodeService.updateBySpecifiedField(regionCodeDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class RegionCodeDTOAndSpecifiedFields {

        private RegionCodeDTO regionCode;
        private Set<String> specifiedFields;
        private String specifiedField;

        private RegionCodeDTO getRegionCode() {
            return regionCode;
        }

        private void setRegionCode(RegionCodeDTO regionCode) {
            this.regionCode = regionCode;
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
