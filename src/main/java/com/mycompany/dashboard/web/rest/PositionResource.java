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
import com.mycompany.dashboard.domain.Position;
import com.mycompany.dashboard.repository.PositionRepository;
import com.mycompany.dashboard.service.CommonConditionQueryService;
import com.mycompany.dashboard.service.PositionQueryService;
import com.mycompany.dashboard.service.PositionService;
import com.mycompany.dashboard.service.criteria.PositionCriteria;
import com.mycompany.dashboard.service.dto.PositionDTO;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

 * 管理实体{@link com.mycompany.dashboard.domain.Position}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "positions", tags = "岗位API接口")
public class PositionResource {

    private final Logger log = LoggerFactory.getLogger(PositionResource.class);

    private static final String ENTITY_NAME = "settingsPosition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PositionService positionService;

    private final PositionRepository positionRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final PositionQueryService positionQueryService;

    public PositionResource(
        PositionService positionService,
        PositionRepository positionRepository,
        CommonConditionQueryService commonConditionQueryService,
        PositionQueryService positionQueryService
    ) {
        this.positionService = positionService;
        this.positionRepository = positionRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.positionQueryService = positionQueryService;
    }

    /**
     * {@code POST  /positions} : Create a new position.
     *
     * @param positionDTO the positionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new positionDTO, or with status {@code 400 (Bad Request)} if the position has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/positions")
    @ApiOperation(value = "新建岗位", notes = "创建并返回一个新的岗位")
    public ResponseEntity<PositionDTO> createPosition(@Valid @RequestBody PositionDTO positionDTO) throws URISyntaxException {
        log.debug("REST request to save Position : {}", positionDTO);
        if (positionDTO.getId() != null) {
            throw new BadRequestAlertException("A new position cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PositionDTO result = positionService.save(positionDTO);
        return ResponseEntity
            .created(new URI("/api/positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /positions/:id} : Updates an existing position.
     *
     * @param id the id of the positionDTO to save.
     * @param positionDTO the positionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionDTO,
     * or with status {@code 400 (Bad Request)} if the positionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the positionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/positions/{id}")
    @ApiOperation(value = "更新岗位", notes = "根据主键更新并返回一个更新后的岗位")
    public ResponseEntity<PositionDTO> updatePosition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PositionDTO positionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Position : {}, {}", id, positionDTO);
        if (positionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionService.exists(Position::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PositionDTO result = positionService.save(positionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /positions/:id} : Partial updates given fields of an existing position, field will ignore if it is null.
     *
     * @param id the id of the positionDTO to save.
     * @param positionDTO the positionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionDTO,
     * or with status {@code 400 (Bad Request)} if the positionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the positionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the positionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新岗位", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的岗位")
    @PatchMapping(value = "/positions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PositionDTO> partialUpdatePosition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PositionDTO positionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Position partially : {}, {}", id, positionDTO);
        if (positionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (positionRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PositionDTO> result = positionService.partialUpdate(positionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /positions} : get all the positions.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of positions in body.
     */
    @GetMapping("/positions")
    @ApiOperation(value = "获取岗位分页列表", notes = "获取岗位的分页列表数据")
    public ResponseEntity<List<PositionDTO>> getAllPositions(
        PositionCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get Positions by criteria: {}", criteria);
        IPage<PositionDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = positionQueryService.selectByCustomEntity(listModelName, criteria, queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = positionQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = positionQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = positionQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /positions/count} : count all the positions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/positions/count")
    public ResponseEntity<Long> countPositions(PositionCriteria criteria) {
        log.debug("REST request to count Positions by criteria: {}", criteria);
        return ResponseEntity.ok().body(positionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /positions/:id} : get the "id" position.
     *
     * @param id the id of the positionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the positionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/positions/{id}")
    @ApiOperation(value = "获取指定主键的岗位", notes = "获取指定主键的岗位信息")
    public ResponseEntity<PositionDTO> getPosition(@PathVariable Long id) {
        log.debug("REST request to get Position : {}", id);
        Optional<PositionDTO> positionDTO = positionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(positionDTO);
    }

    /**
     * GET  /positions/export : export the positions.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the positionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/positions/export")
    @ApiOperation(value = "岗位EXCEL导出", notes = "导出全部岗位为EXCEL文件")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<PositionDTO> page = positionService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("计算机一班学生", "学生"), PositionDTO.class, page.getRecords());
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
     * POST  /positions/import : import the positions from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the positionDTO, or with status 404 (Not Found)
     */
    @PostMapping("/positions/import")
    @ApiOperation(value = "岗位EXCEL导入", notes = "根据岗位EXCEL文件导入全部数据")
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
        List<PositionDTO> list = ExcelImportUtil.importExcel(savedFile, PositionDTO.class, params);
        list.forEach(positionService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /positions/:id} : delete the "id" position.
     *
     * @param id the id of the positionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/positions/{id}")
    @ApiOperation(value = "删除一个岗位", notes = "根据主键删除单个岗位")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        log.debug("REST request to delete Position : {}", id);
        positionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /positions} : delete all the "ids" Positions.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/positions")
    @ApiOperation(value = "删除多个岗位", notes = "根据主键删除多个岗位")
    public ResponseEntity<Void> deletePositionsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete Positions : {}", ids);
        if (ids != null) {
            ids.forEach(positionService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /positions/specified-fields} : Updates an existing position by specified fields.
     *
     * @param positionDTOAndSpecifiedFields the positionDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionDTO,
     * or with status {@code 400 (Bad Request)} if the positionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the positionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/positions/specified-fields")
    @ApiOperation(value = "根据字段部分更新岗位", notes = "根据指定字段部分更新岗位，给定的属性值可以为任何值，包括null")
    public ResponseEntity<PositionDTO> updatePositionBySpecifiedFields(
        @RequestBody PositionDTOAndSpecifiedFields positionDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update Position : {}", positionDTOAndSpecifiedFields);
        PositionDTO positionDTO = positionDTOAndSpecifiedFields.getPosition();
        Set<String> specifiedFields = positionDTOAndSpecifiedFields.getSpecifiedFields();
        if (positionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PositionDTO result = positionService.updateBySpecifiedFields(positionDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, positionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /positions/specified-field} : Updates an existing position by specified field.
     *
     * @param positionDTOAndSpecifiedFields the positionDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionDTO,
     * or with status {@code 400 (Bad Request)} if the positionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the positionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/positions/specified-field")
    @ApiOperation(value = "更新岗位单个属性", notes = "根据指定字段更新岗位，给定的属性值可以为任何值，包括null")
    public ResponseEntity<PositionDTO> updatePositionBySpecifiedField(
        @RequestBody PositionDTOAndSpecifiedFields positionDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update Position : {}", positionDTOAndSpecifiedFields);
        PositionDTO positionDTO = positionDTOAndSpecifiedFields.getPosition();
        String fieldName = positionDTOAndSpecifiedFields.getSpecifiedField();
        if (positionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PositionDTO result = positionService.updateBySpecifiedField(positionDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class PositionDTOAndSpecifiedFields {

        private PositionDTO position;
        private Set<String> specifiedFields;
        private String specifiedField;

        private PositionDTO getPosition() {
            return position;
        }

        private void setPosition(PositionDTO position) {
            this.position = position;
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
