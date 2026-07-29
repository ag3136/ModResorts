package com.acme.modres.mbean;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpMetadataListTest {

    private OpMetadataList opMetadataList;

    @BeforeEach
    void setUp() {
        opMetadataList = new OpMetadataList();
    }

    @Test
    void testDefaultConstructor_shouldCreateInstance() {
        // Assert
        assertNotNull(opMetadataList);
        assertNotNull(opMetadataList.getOpMetadatList());
    }

    @Test
    void testGetOpMetadatList_shouldReturnEmptyList() {
        // Act
        List<OpMetadata> result = opMetadataList.getOpMetadatList();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testAdd_shouldAddOpMetadata() {
        // Arrange
        OpMetadata metadata = new OpMetadata("testOp", "Test", "void", 1);

        // Act
        opMetadataList.add(metadata);

        // Assert
        assertEquals(1, opMetadataList.getOpMetadatList().size());
        assertEquals(metadata, opMetadataList.getOpMetadatList().get(0));
    }

    @Test
    void testAdd_multipleItems_shouldAddAll() {
        // Arrange
        OpMetadata metadata1 = new OpMetadata("op1", "Test1", "void", 1);
        OpMetadata metadata2 = new OpMetadata("op2", "Test2", "String", 2);
        OpMetadata metadata3 = new OpMetadata("op3", "Test3", "int", 3);

        // Act
        opMetadataList.add(metadata1);
        opMetadataList.add(metadata2);
        opMetadataList.add(metadata3);

        // Assert
        assertEquals(3, opMetadataList.getOpMetadatList().size());
    }

    @Test
    void testSetOpMetadatList_shouldReplaceList() {
        // Arrange
        List<OpMetadata> newList = new java.util.ArrayList<>();
        newList.add(new OpMetadata("op1", "Test1", "void", 1));
        newList.add(new OpMetadata("op2", "Test2", "String", 2));

        // Act
        opMetadataList.setOpMetadatList(newList);

        // Assert
        assertEquals(2, opMetadataList.getOpMetadatList().size());
        assertEquals(newList, opMetadataList.getOpMetadatList());
    }

    @Test
    void testAdd_withNullMetadata_shouldAddNull() {
        // Act
        opMetadataList.add(null);

        // Assert
        assertEquals(1, opMetadataList.getOpMetadatList().size());
        assertNull(opMetadataList.getOpMetadatList().get(0));
    }

    @Test
    void testSetOpMetadatList_withNull_shouldSetNull() {
        // Act
        opMetadataList.setOpMetadatList(null);

        // Assert
        assertNull(opMetadataList.getOpMetadatList());
    }

    @Test
    void testAdd_afterSetOpMetadatList_shouldAddToNewList() {
        // Arrange
        List<OpMetadata> newList = new java.util.ArrayList<>();
        opMetadataList.setOpMetadatList(newList);
        OpMetadata metadata = new OpMetadata("op1", "Test", "void", 1);

        // Act
        opMetadataList.add(metadata);

        // Assert
        assertEquals(1, opMetadataList.getOpMetadatList().size());
    }

    @Test
    void testGetOpMetadatList_shouldReturnMutableList() {
        // Act
        List<OpMetadata> list = opMetadataList.getOpMetadatList();
        OpMetadata metadata = new OpMetadata("op1", "Test", "void", 1);
        list.add(metadata);

        // Assert
        assertEquals(1, opMetadataList.getOpMetadatList().size());
    }

    @Test
    void testAdd_multipleTimesWithSameObject_shouldAddMultipleTimes() {
        // Arrange
        OpMetadata metadata = new OpMetadata("op1", "Test", "void", 1);

        // Act
        opMetadataList.add(metadata);
        opMetadataList.add(metadata);
        opMetadataList.add(metadata);

        // Assert
        assertEquals(3, opMetadataList.getOpMetadatList().size());
    }
}
