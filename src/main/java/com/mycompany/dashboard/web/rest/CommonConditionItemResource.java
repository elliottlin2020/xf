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
import com.mycompany.dashboard.domain.CommonConditionItem;
import com.mycompany.dashboard.repository.CommonConditionItemRepository;
import com.mycompany.dashboard.service.CommonConditionItemQueryService;
import com.mycompany.dashboard.service.CommonConditionItemService;
import com.mycompany.dashboard.service.CommonConditionQueryService;
import com.mycompany.dashboard.service.criteria.CommonConditionItemCriteria;
import com.mycompany.dashboard.service.dto.CommonConditionItemDTO;
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

 * 管理实体{@link com.mycompany.dashboard.domain.CommonConditionItem}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "common-condition-items", tags = "通用条件条目API接口")
public class CommonConditionItemResource {

    private final Logger log = LoggerFactory.getLogger(CommonConditionItemResource.class);

    private static final String ENTITY_NAME = "reportCommonConditionItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommonConditionItemService commonConditionItemService;

    private final CommonConditionItemRepository commonConditionItemRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final CommonConditionItemQueryService commonConditionItemQueryService;

    public CommonConditionItemResource(
        CommonConditionItemService commonConditionItemService,
        CommonConditionItemRepository commonConditionItemRepository,
        CommonConditionQueryService commonConditionQueryService,
        CommonConditionItemQueryService commonConditionItemQueryService
    ) {
        this.commonConditionItemService = commonConditionItemService;
        this.commonConditionItemRepository = commonConditionItemRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.commonConditionItemQueryService = commonConditionItemQueryService;
    }

    /**
     * {@code POST  /common-condition-items} : Create a new commonConditionItem.
     *
     * @param commonConditionItemDTO the commonConditionItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commonConditionItemDTO, or with status {@code 400 (Bad Request)} if the commonConditionItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/common-condition-items")
    @ApiOperation(value = "新建通用条件条目", notes = "创建并返回一个新的通用条件条目")
    public ResponseEntity<CommonConditionItemDTO> createCommonConditionItem(@RequestBody CommonConditionItemDTO commonConditionItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save CommonConditionItem : {}", commonConditionItemDTO);
        if (commonConditionItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new commonConditionItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommonConditionItemDTO result = commonConditionItemService.save(commonConditionItemDTO);
        return ResponseEntity
            .created(new URI("/api/common-condition-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /common-condition-items/:id} : Updates an existing commonConditionItem.
     *
     * @param id the id of the commonConditionItemDTO to save.
     * @param commonConditionItemDTO the commonConditionItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonConditionItemDTO,
     * or with status {@code 400 (Bad Request)} if the commonConditionItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonConditionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-condition-items/{id}")
    @ApiOperation(value = "更新通用条件条目", notes = "根据主键更新并返回一个更新后的通用条件条目")
    public ResponseEntity<CommonConditionItemDTO> updateCommonConditionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommonConditionItemDTO commonConditionItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommonConditionItem : {}, {}", id, commonConditionItemDTO);
        if (commonConditionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commonConditionItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commonConditionItemService.exists(CommonConditionItem::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommonConditionItemDTO result = commonConditionItemService.save(commonConditionItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, commonConditionItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /common-condition-items/:id} : Partial updates given fields of an existing commonConditionItem, field will ignore if it is null.
     *
     * @param id the id of the commonConditionItemDTO to save.
     * @param commonConditionItemDTO the commonConditionItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonConditionItemDTO,
     * or with status {@code 400 (Bad Request)} if the commonConditionItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commonConditionItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commonConditionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(
        value = "部分更新通用条件条目",
        notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的通用条件条目"
    )
    @PatchMapping(value = "/common-condition-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CommonConditionItemDTO> partialUpdateCommonConditionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommonConditionItemDTO commonConditionItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommonConditionItem partially : {}, {}", id, commonConditionItemDTO);
        if (commonConditionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commonConditionItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (commonConditionItemRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommonConditionItemDTO> result = commonConditionItemService.partialUpdate(commonConditionItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, commonConditionItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /common-condition-items} : get all the commonConditionItems.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commonConditionItems in body.
     */
    @GetMapping("/common-condition-items")
    @ApiOperation(value = "获取通用条件条目分页列表", notes = "获取通用条件条目的分页列表数据")
    public ResponseEntity<List<CommonConditionItemDTO>> getAllCommonConditionItems(
        CommonConditionItemCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get CommonConditionItems by criteria: {}", criteria);
        IPage<CommonConditionItemDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page =
                    commonConditionItemQueryService.selectByCustomEntity(
                        listModelName,
                        criteria,
                        queryWrapper,
                        PageableUtils.toPage(pageable)
                    );
            } else {
                page = commonConditionItemQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = commonConditionItemQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = commonConditionItemQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /common-condition-items/count} : count all the commonConditionItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/common-condition-items/count")
    public ResponseEntity<Long> countCommonConditionItems(CommonConditionItemCriteria criteria) {
        log.debug("REST request to count CommonConditionItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(commonConditionItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /common-condition-items/:id} : get the "id" commonConditionItem.
     *
     * @param id the id of the commonConditionItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commonConditionItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/common-condition-items/{id}")
    @ApiOperation(value = "获取指定主键的通用条件条目", notes = "获取指定主键的通用条件条目信息")
    public ResponseEntity<CommonConditionItemDTO> getCommonConditionItem(@PathVariable Long id) {
        log.debug("REST request to get CommonConditionItem : {}", id);
        Optional<CommonConditionItemDTO> commonConditionItemDTO = commonConditionItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commonConditionItemDTO);
    }

    /**
     * GET  /common-condition-items/export : export the commonConditionItems.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the commonConditionItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/common-condition-items/export")
    @ApiOperation(value = "通用条件条目EXCEL导出", notes = "导出全部通用条件条目为EXCEL文件")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<CommonConditionItemDTO> page = commonConditionItemService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("计算机一班学生", "学生"),
            CommonConditionItemDTO.class,
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
     * POST  /common-condition-items/import : import the commonConditionItems from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the commonConditionItemDTO, or with status 404 (Not Found)
     */
    @PostMapping("/common-condition-items/import")
    @ApiOperation(value = "通用条件条目EXCEL导入", notes = "根据通用条件条目EXCEL文件导入全部数据")
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
        List<CommonConditionItemDTO> list = ExcelImportUtil.importExcel(savedFile, CommonConditionItemDTO.class, params);
        list.forEach(commonConditionItemService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /common-condition-items/:id} : delete the "id" commonConditionItem.
     *
     * @param id the id of the commonConditionItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/common-condition-items/{id}")
    @ApiOperation(value = "删除一个通用条件条目", notes = "根据主键删除单个通用条件条目")
    public ResponseEntity<Void> deleteCommonConditionItem(@PathVariable Long id) {
        log.debug("REST request to delete CommonConditionItem : {}", id);
        commonConditionItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /common-condition-items} : delete all the "ids" CommonConditionItems.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/common-condition-items")
    @ApiOperation(value = "删除多个通用条件条目", notes = "根据主键删除多个通用条件条目")
    public ResponseEntity<Void> deleteCommonConditionItemsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete CommonConditionItems : {}", ids);
        if (ids != null) {
            ids.forEach(commonConditionItemService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /common-condition-items/specified-fields} : Updates an existing commonConditionItem by specified fields.
     *
     * @param commonConditionItemDTOAndSpecifiedFields the commonConditionItemDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonConditionItemDTO,
     * or with status {@code 400 (Bad Request)} if the commonConditionItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonConditionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-condition-items/specified-fields")
    @ApiOperation(value = "根据字段部分更新通用条件条目", notes = "根据指定字段部分更新通用条件条目，给定的属性值可以为任何值，包括null")
    public ResponseEntity<CommonConditionItemDTO> updateCommonConditionItemBySpecifiedFields(
        @RequestBody CommonConditionItemDTOAndSpecifiedFields commonConditionItemDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update CommonConditionItem : {}", commonConditionItemDTOAndSpecifiedFields);
        CommonConditionItemDTO commonConditionItemDTO = commonConditionItemDTOAndSpecifiedFields.getCommonConditionItem();
        Set<String> specifiedFields = commonConditionItemDTOAndSpecifiedFields.getSpecifiedFields();
        if (commonConditionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommonConditionItemDTO result = commonConditionItemService.updateBySpecifiedFields(commonConditionItemDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonConditionItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /common-condition-items/specified-field} : Updates an existing commonConditionItem by specified field.
     *
     * @param commonConditionItemDTOAndSpecifiedFields the commonConditionItemDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonConditionItemDTO,
     * or with status {@code 400 (Bad Request)} if the commonConditionItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonConditionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-condition-items/specified-field")
    @ApiOperation(value = "更新通用条件条目单个属性", notes = "根据指定字段更新通用条件条目，给定的属性值可以为任何值，包括null")
    public ResponseEntity<CommonConditionItemDTO> updateCommonConditionItemBySpecifiedField(
        @RequestBody CommonConditionItemDTOAndSpecifiedFields commonConditionItemDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update CommonConditionItem : {}", commonConditionItemDTOAndSpecifiedFields);
        CommonConditionItemDTO commonConditionItemDTO = commonConditionItemDTOAndSpecifiedFields.getCommonConditionItem();
        String fieldName = commonConditionItemDTOAndSpecifiedFields.getSpecifiedField();
        if (commonConditionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommonConditionItemDTO result = commonConditionItemService.updateBySpecifiedField(commonConditionItemDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class CommonConditionItemDTOAndSpecifiedFields {

        private CommonConditionItemDTO commonConditionItem;
        private Set<String> specifiedFields;
        private String specifiedField;

        private CommonConditionItemDTO getCommonConditionItem() {
            return commonConditionItem;
        }

        private void setCommonConditionItem(CommonConditionItemDTO commonConditionItem) {
            this.commonConditionItem = commonConditionItem;
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
