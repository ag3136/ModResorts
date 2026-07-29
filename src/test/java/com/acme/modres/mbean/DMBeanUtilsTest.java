package com.acme.modres.mbean;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanOperationInfo;

import org.junit.jupiter.api.Test;

class DMBeanUtilsTest {

    @Test
    void testGetOps_withNullOpList_shouldReturnNull() {
        // Act
        MBeanOperationInfo[] result = DMBeanUtils.getOps(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetOps_withNullOpMetadataList_shouldReturnNull() {
        // Arrange
        OpMetadataList opList = new OpMetadataList();
        opList.setOpMetadatList(null);

        // Act
        MBeanOperationInfo[] result = DMBeanUtils.getOps(opList);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetOps_withEmptyOpList_shouldReturnNull() {
        // Arrange
        OpMetadataList opList = new OpMetadataList();

        // Act
        MBeanOperationInfo[] result = DMBeanUtils.getOps(opList);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetOps_withSingleOperation_shouldReturnArray() {
        // Arrange
        OpMetadataList opList = new OpMetadataList();
        OpMetadata opMetadata = new OpMetadata("testOp", "Test operation", "void", 1);
        opList.add(opMetadata);

        // Act
        MBeanOperationInfo[] result = DMBeanUtils.getOps(opList);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("testOp", result[0].getName());
        assertEquals("Test operation", result[0].getDescription());
    }

    @Test
    void testGetOps_withMultipleOperations_shouldReturnArray() {
        // Arrange
        OpMetadataList opList = new OpMetadataList();
        opList.add(new OpMetadata("op1", "Operation 1", "void", 1));
        opList.add(new OpMetadata("op2", "Operation 2", "String", 2));
        opList.add(new OpMetadata("op3", "Operation 3", "int", 3));

        // Act
        MBeanOperationInfo[] result = DMBeanUtils.getOps(opList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("op1", result[0].getName());
        assertEquals("op2", result[1].getName());
        assertEquals("op3", result[2].getName());
    }

    @Test
    void testGetOps_shouldPreserveOperationDetails() {
        // Arrange
        OpMetadataList opList = new OpMetadataList();
        OpMetadata opMetadata = new OpMetadata("increaseLimit", "Increase the limit", "String", 2);
        opList.add(opMetadata);

        // Act
        MBeanOperationInfo[] result = DMBeanUtils.getOps(opList);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("increaseLimit", result[0].getName());
        assertEquals("Increase the limit", result[0].getDescription());
        assertEquals("String", result[0].getReturnType());
        assertEquals(2, result[0].getImpact());
    }

    @Test
    void testGetOps_withDifferentImpactLevels_shouldHandleCorrectly() {
        // Arrange
        OpMetadataList opList = new OpMetadataList();
        opList.add(new OpMetadata("op1", "Desc1", "void", 0));
        opList.add(new OpMetadata("op2", "Desc2", "void", 1));
        opList.add(new OpMetadata("op3", "Desc3", "void", 2));

        // Act
        MBeanOperationInfo[] result = DMBeanUtils.getOps(opList);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(0, result[0].getImpact());
        assertEquals(1, result[1].getImpact());
        assertEquals(2, result[2].getImpact());
    }
}
