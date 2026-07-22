package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.work.domain.WorkPositionType;
import com.ruoyi.system.work.service.IWorkPositionTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ModelMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * WorkPositionTypeController 单元测试
 */
@ExtendWith(MockitoExtension.class)
class WorkPositionTypeControllerTest
{
    @Mock
    private IWorkPositionTypeService positionTypeService;

    @InjectMocks
    private WorkPositionTypeController controller;

    private WorkPositionType workPositionType;

    @BeforeEach
    void setUp()
    {
        workPositionType = new WorkPositionType();
        workPositionType.setTypeId(1L);
        workPositionType.setTypeCode("DEV");
        workPositionType.setTypeName("开发");
        workPositionType.setSortOrder(1);
        workPositionType.setStatus("0");
    }

    @Test
    void testOperlog_ShouldReturnViewPath()
    {
        String result = controller.operlog();
        assertThat(result).isEqualTo("system/work/positionType/positionType");
    }

    @Test
    void testList_ShouldReturnTableDataInfo()
    {
        List<WorkPositionType> mockList = Arrays.asList(workPositionType);
        when(positionTypeService.selectPositionTypeList(any(WorkPositionType.class))).thenReturn(mockList);

        TableDataInfo result = controller.list(new WorkPositionType());

        assertThat(result.getCode()).isEqualTo(0);
        assertThat(result.getRows()).hasSize(1);
        assertThat(result.getTotal()).isEqualTo(1);
        verify(positionTypeService, times(1)).selectPositionTypeList(any(WorkPositionType.class));
    }

    @Test
    void testList_WhenEmpty_ShouldReturnEmptyTableDataInfo()
    {
        when(positionTypeService.selectPositionTypeList(any(WorkPositionType.class)))
                .thenReturn(Collections.emptyList());

        TableDataInfo result = controller.list(new WorkPositionType());

        assertThat(result.getCode()).isEqualTo(0);
        assertThat(result.getRows()).isEmpty();
        assertThat(result.getTotal()).isEqualTo(0);
    }

    @Test
    void testRemove_ShouldReturnSuccess()
    {
        when(positionTypeService.deletePositionTypeByIds(anyString())).thenReturn(1);

        AjaxResult result = controller.remove("1,2,3");

        assertThat(result.get(AjaxResult.CODE_TAG)).isEqualTo(0);
        verify(positionTypeService, times(1)).deletePositionTypeByIds("1,2,3");
    }

    @Test
    void testRemove_WhenDeleteZero_ShouldReturnError()
    {
        when(positionTypeService.deletePositionTypeByIds(anyString())).thenReturn(0);

        AjaxResult result = controller.remove("999");

        assertThat(result.get(AjaxResult.CODE_TAG)).isEqualTo(500);
    }

    @Test
    void testAdd_ShouldReturnAddViewPath()
    {
        String result = controller.add();
        assertThat(result).isEqualTo("system/work/positionType/add");
    }

    @Test
    void testAddSave_ShouldSetCreateByAndUpdateByThenInsert()
    {
        // addSave 中调用了 getLoginName()，需要 mock SecurityUtils
        try (var mockedShiro = mockStatic(com.ruoyi.common.utils.ShiroUtils.class)) {
            var sysUser = new com.ruoyi.common.core.domain.entity.SysUser();
            sysUser.setLoginName("admin");
            mockedShiro.when(com.ruoyi.common.utils.ShiroUtils::getSysUser).thenReturn(sysUser);
            when(positionTypeService.insertPositionType(any(WorkPositionType.class))).thenReturn(1);

            AjaxResult result = controller.addSave(workPositionType);

            assertThat(result.get(AjaxResult.CODE_TAG)).isEqualTo(0);
            // 验证 createBy 和 updateBy 已被设置
            assertThat(workPositionType.getCreateBy()).isEqualTo("admin");
            assertThat(workPositionType.getUpdateBy()).isEqualTo("admin");
            verify(positionTypeService, times(1)).insertPositionType(workPositionType);
        }
    }

    @Test
    void testEdit_ShouldReturnEditViewWithModel()
    {
        when(positionTypeService.selectPositionTypeById(anyLong())).thenReturn(workPositionType);

        ModelMap mmap = new ModelMap();
        String result = controller.edit(1L, mmap);

        assertThat(result).isEqualTo("system/work/positionType/edit");
        assertThat(mmap.get("positionType")).isEqualTo(workPositionType);
        verify(positionTypeService, times(1)).selectPositionTypeById(1L);
    }

    @Test
    void testEditSave_ShouldSetUpdateByThenUpdate()
    {
        // editSave 中调用了 getLoginName()，需要 mock SecurityUtils
        try (var mockedShiro = mockStatic(com.ruoyi.common.utils.ShiroUtils.class)) {
            var sysUser = new com.ruoyi.common.core.domain.entity.SysUser();
            sysUser.setLoginName("admin");
            mockedShiro.when(com.ruoyi.common.utils.ShiroUtils::getSysUser).thenReturn(sysUser);
            when(positionTypeService.updatePositionType(any(WorkPositionType.class))).thenReturn(1);

            AjaxResult result = controller.editSave(workPositionType);

            assertThat(result.get(AjaxResult.CODE_TAG)).isEqualTo(0);
            // 验证 updateBy 已被设置
            assertThat(workPositionType.getUpdateBy()).isEqualTo("admin");
            verify(positionTypeService, times(1)).updatePositionType(workPositionType);
        }
    }

    @Test
    void testListAll_ShouldReturnAllPositionTypes()
    {
        List<WorkPositionType> mockList = Arrays.asList(workPositionType);
        when(positionTypeService.selectPositionTypeAll()).thenReturn(mockList);

        AjaxResult result = controller.listAll();

        assertThat(result.get(AjaxResult.CODE_TAG)).isEqualTo(0);
        @SuppressWarnings("unchecked")
        List<WorkPositionType> data = (List<WorkPositionType>) result.get(AjaxResult.DATA_TAG);
        assertThat(data).hasSize(1);
        verify(positionTypeService, times(1)).selectPositionTypeAll();
    }

    @Test
    void testCheckTypeCodeUnique_WhenUnique_ShouldReturnTrue()
    {
        when(positionTypeService.checkTypeCodeUnique(any(WorkPositionType.class))).thenReturn(true);

        boolean result = controller.checkTypeCodeUnique(workPositionType);

        assertThat(result).isTrue();
        verify(positionTypeService, times(1)).checkTypeCodeUnique(workPositionType);
    }

    @Test
    void testCheckTypeCodeUnique_WhenNotUnique_ShouldReturnFalse()
    {
        when(positionTypeService.checkTypeCodeUnique(any(WorkPositionType.class))).thenReturn(false);

        boolean result = controller.checkTypeCodeUnique(workPositionType);

        assertThat(result).isFalse();
        verify(positionTypeService, times(1)).checkTypeCodeUnique(workPositionType);
    }
}