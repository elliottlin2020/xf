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
import com.mycompany.dashboard.domain.DepartmentAuthority;
import com.mycompany.dashboard.repository.DepartmentAuthorityRepository;
import com.mycompany.dashboard.service.CommonConditionQueryService;
import com.mycompany.dashboard.service.DepartmentAuthorityQueryService;
import com.mycompany.dashboard.service.DepartmentAuthorityService;
import com.mycompany.dashboard.service.criteria.DepartmentAuthorityCriteria;
import com.mycompany.dashboard.service.dto.DepartmentAuthorityDTO;
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

 * 管理实体{@link com.mycompany.dashboard.domain.DepartmentAuthority}的REST Controller。
 */
@RestController
@RequestMapping("/api")
@Api(value = "department-authorities", tags = "部门角色API接口")
public class DepartmentAuthorityResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentAuthorityResource.class);

    private static final String ENTITY_NAME = "settingsDepartmentAuthority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentAuthorityService departmentAuthorityService;

    private final DepartmentAuthorityRepository departmentAuthorityRepository;

    private final CommonConditionQueryService commonConditionQueryService;

    private final DepartmentAuthorityQueryService departmentAuthorityQueryService;

    public DepartmentAuthorityResource(
        DepartmentAuthorityService departmentAuthorityService,
        DepartmentAuthorityRepository departmentAuthorityRepository,
        CommonConditionQueryService commonConditionQueryService,
        DepartmentAuthorityQueryService departmentAuthorityQueryService
    ) {
        this.departmentAuthorityService = departmentAuthorityService;
        this.departmentAuthorityRepository = departmentAuthorityRepository;
        this.commonConditionQueryService = commonConditionQueryService;
        this.departmentAuthorityQueryService = departmentAuthorityQueryService;
    }

    /**
     * {@code POST  /department-authorities} : Create a new departmentAuthority.
     *
     * @param departmentAuthorityDTO the departmentAuthorityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departmentAuthorityDTO, or with status {@code 400 (Bad Request)} if the departmentAuthority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/department-authorities")
    @ApiOperation(value = "新建部门角色", notes = "创建并返回一个新的部门角色")
    public ResponseEntity<DepartmentAuthorityDTO> createDepartmentAuthority(@RequestBody DepartmentAuthorityDTO departmentAuthorityDTO)
        throws URISyntaxException {
        log.debug("REST request to save DepartmentAuthority : {}", departmentAuthorityDTO);
        if (departmentAuthorityDTO.getId() != null) {
            throw new BadRequestAlertException("A new departmentAuthority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartmentAuthorityDTO result = departmentAuthorityService.save(departmentAuthorityDTO);
        return ResponseEntity
            .created(new URI("/api/department-authorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /department-authorities/:id} : Updates an existing departmentAuthority.
     *
     * @param id the id of the departmentAuthorityDTO to save.
     * @param departmentAuthorityDTO the departmentAuthorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentAuthorityDTO,
     * or with status {@code 400 (Bad Request)} if the departmentAuthorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentAuthorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/department-authorities/{id}")
    @ApiOperation(value = "更新部门角色", notes = "根据主键更新并返回一个更新后的部门角色")
    public ResponseEntity<DepartmentAuthorityDTO> updateDepartmentAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepartmentAuthorityDTO departmentAuthorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DepartmentAuthority : {}, {}", id, departmentAuthorityDTO);
        if (departmentAuthorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentAuthorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentAuthorityService.exists(DepartmentAuthority::getId, id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepartmentAuthorityDTO result = departmentAuthorityService.save(departmentAuthorityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, departmentAuthorityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /department-authorities/:id} : Partial updates given fields of an existing departmentAuthority, field will ignore if it is null.
     *
     * @param id the id of the departmentAuthorityDTO to save.
     * @param departmentAuthorityDTO the departmentAuthorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentAuthorityDTO,
     * or with status {@code 400 (Bad Request)} if the departmentAuthorityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the departmentAuthorityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the departmentAuthorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @ApiOperation(value = "部分更新部门角色", notes = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的部门角色")
    @PatchMapping(value = "/department-authorities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DepartmentAuthorityDTO> partialUpdateDepartmentAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepartmentAuthorityDTO departmentAuthorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DepartmentAuthority partially : {}, {}", id, departmentAuthorityDTO);
        if (departmentAuthorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentAuthorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (departmentAuthorityRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepartmentAuthorityDTO> result = departmentAuthorityService.partialUpdate(departmentAuthorityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, departmentAuthorityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /department-authorities} : get all the departmentAuthorities.
     *

     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departmentAuthorities in body.
     */
    @GetMapping("/department-authorities")
    @ApiOperation(value = "获取部门角色分页列表", notes = "获取部门角色的分页列表数据")
    public ResponseEntity<List<DepartmentAuthorityDTO>> getAllDepartmentAuthorities(
        DepartmentAuthorityCriteria criteria,
        Pageable pageable,
        @RequestParam(value = "listModelName", required = false) String listModelName,
        @RequestParam(value = "commonQueryId", required = false) Long commonQueryId
    ) throws ClassNotFoundException {
        log.debug("REST request to get DepartmentAuthorities by criteria: {}", criteria);
        IPage<DepartmentAuthorityDTO> page;
        if (listModelName != null) {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page =
                    departmentAuthorityQueryService.selectByCustomEntity(
                        listModelName,
                        criteria,
                        queryWrapper,
                        PageableUtils.toPage(pageable)
                    );
            } else {
                page = departmentAuthorityQueryService.selectByCustomEntity(listModelName, criteria, null, PageableUtils.toPage(pageable));
            }
        } else {
            if (commonQueryId != null) {
                QueryWrapper queryWrapper = commonConditionQueryService.createQueryWrapper(commonQueryId);
                page = departmentAuthorityQueryService.findByQueryWrapper(queryWrapper, PageableUtils.toPage(pageable));
            } else {
                page = departmentAuthorityQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
            }
        }
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getRecords());
    }

    /**
     * {@code GET  /department-authorities/count} : count all the departmentAuthorities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/department-authorities/count")
    public ResponseEntity<Long> countDepartmentAuthorities(DepartmentAuthorityCriteria criteria) {
        log.debug("REST request to count DepartmentAuthorities by criteria: {}", criteria);
        return ResponseEntity.ok().body(departmentAuthorityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /department-authorities/:id} : get the "id" departmentAuthority.
     *
     * @param id the id of the departmentAuthorityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departmentAuthorityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/department-authorities/{id}")
    @ApiOperation(value = "获取指定主键的部门角色", notes = "获取指定主键的部门角色信息")
    public ResponseEntity<DepartmentAuthorityDTO> getDepartmentAuthority(@PathVariable Long id) {
        log.debug("REST request to get DepartmentAuthority : {}", id);
        Optional<DepartmentAuthorityDTO> departmentAuthorityDTO = departmentAuthorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentAuthorityDTO);
    }

    /**
     * GET  /department-authorities/export : export the departmentAuthorities.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the departmentAuthorityDTO, or with status 404 (Not Found)
     */
    @GetMapping("/department-authorities/export")
    @ApiOperation(value = "部门角色EXCEL导出", notes = "导出全部部门角色为EXCEL文件")
    public ResponseEntity<Void> exportToExcel() throws IOException {
        IPage<DepartmentAuthorityDTO> page = departmentAuthorityService.findAll(new Page<>(1, Integer.MAX_VALUE));
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("计算机一班学生", "学生"),
            DepartmentAuthorityDTO.class,
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
     * POST  /department-authorities/import : import the departmentAuthorities from excel file.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and with body the departmentAuthorityDTO, or with status 404 (Not Found)
     */
    @PostMapping("/department-authorities/import")
    @ApiOperation(value = "部门角色EXCEL导入", notes = "根据部门角色EXCEL文件导入全部数据")
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
        List<DepartmentAuthorityDTO> list = ExcelImportUtil.importExcel(savedFile, DepartmentAuthorityDTO.class, params);
        list.forEach(departmentAuthorityService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /department-authorities/:id} : delete the "id" departmentAuthority.
     *
     * @param id the id of the departmentAuthorityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/department-authorities/{id}")
    @ApiOperation(value = "删除一个部门角色", notes = "根据主键删除单个部门角色")
    public ResponseEntity<Void> deleteDepartmentAuthority(@PathVariable Long id) {
        log.debug("REST request to delete DepartmentAuthority : {}", id);
        departmentAuthorityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /department-authorities} : delete all the "ids" DepartmentAuthorities.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/department-authorities")
    @ApiOperation(value = "删除多个部门角色", notes = "根据主键删除多个部门角色")
    public ResponseEntity<Void> deleteDepartmentAuthoritiesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete DepartmentAuthorities : {}", ids);
        if (ids != null) {
            ids.forEach(departmentAuthorityService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    /**
     * {@code PUT  /department-authorities/specified-fields} : Updates an existing departmentAuthority by specified fields.
     *
     * @param departmentAuthorityDTOAndSpecifiedFields the departmentAuthorityDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentAuthorityDTO,
     * or with status {@code 400 (Bad Request)} if the departmentAuthorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentAuthorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/department-authorities/specified-fields")
    @ApiOperation(value = "根据字段部分更新部门角色", notes = "根据指定字段部分更新部门角色，给定的属性值可以为任何值，包括null")
    public ResponseEntity<DepartmentAuthorityDTO> updateDepartmentAuthorityBySpecifiedFields(
        @RequestBody DepartmentAuthorityDTOAndSpecifiedFields departmentAuthorityDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update DepartmentAuthority : {}", departmentAuthorityDTOAndSpecifiedFields);
        DepartmentAuthorityDTO departmentAuthorityDTO = departmentAuthorityDTOAndSpecifiedFields.getDepartmentAuthority();
        Set<String> specifiedFields = departmentAuthorityDTOAndSpecifiedFields.getSpecifiedFields();
        if (departmentAuthorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepartmentAuthorityDTO result = departmentAuthorityService.updateBySpecifiedFields(departmentAuthorityDTO, specifiedFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentAuthorityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /department-authorities/specified-field} : Updates an existing departmentAuthority by specified field.
     *
     * @param departmentAuthorityDTOAndSpecifiedFields the departmentAuthorityDTO and specifiedFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentAuthorityDTO,
     * or with status {@code 400 (Bad Request)} if the departmentAuthorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentAuthorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/department-authorities/specified-field")
    @ApiOperation(value = "更新部门角色单个属性", notes = "根据指定字段更新部门角色，给定的属性值可以为任何值，包括null")
    public ResponseEntity<DepartmentAuthorityDTO> updateDepartmentAuthorityBySpecifiedField(
        @RequestBody DepartmentAuthorityDTOAndSpecifiedFields departmentAuthorityDTOAndSpecifiedFields
    ) throws URISyntaxException {
        log.debug("REST request to update DepartmentAuthority : {}", departmentAuthorityDTOAndSpecifiedFields);
        DepartmentAuthorityDTO departmentAuthorityDTO = departmentAuthorityDTOAndSpecifiedFields.getDepartmentAuthority();
        String fieldName = departmentAuthorityDTOAndSpecifiedFields.getSpecifiedField();
        if (departmentAuthorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepartmentAuthorityDTO result = departmentAuthorityService.updateBySpecifiedField(departmentAuthorityDTO, fieldName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

    private static class DepartmentAuthorityDTOAndSpecifiedFields {

        private DepartmentAuthorityDTO departmentAuthority;
        private Set<String> specifiedFields;
        private String specifiedField;

        private DepartmentAuthorityDTO getDepartmentAuthority() {
            return departmentAuthority;
        }

        private void setDepartmentAuthority(DepartmentAuthorityDTO departmentAuthority) {
            this.departmentAuthority = departmentAuthority;
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
