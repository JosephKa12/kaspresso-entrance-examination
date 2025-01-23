package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val containerCapacity = 10f

    private val storageCapacity = 20f

    private val storage = CerealStorageImpl(containerCapacity, storageCapacity)


    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-1.1f, 10f)
        }
    }

    @Test
    fun `should throw if storageCapacity is lower than containerCapacity`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(6.6f, 3.3f)
        }
    }

    // Tests for addCereal

    @Test
    fun `addCereal should throw if added amount of cereal is lower than 0`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.RICE, -1.1f)
        }
    }

    @Test
    fun `addCereal should throw if no more containers can be placed in storage`() {
        storage.addCereal(Cereal.BULGUR, 10f)
        storage.addCereal(Cereal.RICE, 10f)

        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.PEAS, 10f)
        }
    }

    @Test
    fun `addCereal should throw if no more containers can be placed in storage (added container is hollow)`() {
        storage.addCereal(Cereal.BULGUR, 10f)
        storage.addCereal(Cereal.RICE, 10f)

        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.PEAS, 0f)
        }
    }

    @Test
    fun `addCereal return 0f as excessive amount if container is added and containerCapacity is not exceeded`() {

        val excessiveAmount = storage.addCereal(Cereal.RICE, 1.1f)

        assertEquals(0f, excessiveAmount)
    }

    @Test
    fun `addCereal return 0f as excessive amount if container is added, added amount is 0f`() {

        val excessiveAmount = storage.addCereal(Cereal.RICE, 0f)

        assertEquals(0f, excessiveAmount)
    }

    @Test
    fun `addCereal returns correct excessive amount if container is added and capacity is lower than added amount`() {
        val amount = 12.2f

        val excessiveAmount = storage.addCereal(Cereal.RICE, amount)

        assertEquals(amount - containerCapacity, excessiveAmount)
    }

    @Test
    fun `addCereal returns 0f if container exist and capacity is not exceeded`() {
        storage.addCereal(Cereal.RICE, 1.1f)

        val excessiveAmount = storage.addCereal(Cereal.RICE, 2f)

        assertEquals(0f, excessiveAmount)
    }

    @Test
    fun `addCereal returns 0f if container exist and amount is 0f`() {
        storage.addCereal(Cereal.RICE, 1.1f)

        val excessiveAmount = storage.addCereal(Cereal.RICE, 0f)

        assertEquals(0f, excessiveAmount)
    }

    @Test
    fun `addCereal returns correct excessive amount if container exist and capacity is exceeded`() {

        val initialAmount = 3.3f
        val addedAmount = 8.8f

        storage.addCereal(Cereal.RICE, initialAmount)

        val excessiveAmount = storage.addCereal(Cereal.RICE, addedAmount)

        val expectedValue =  initialAmount + addedAmount - containerCapacity

        assertEquals(expectedValue, excessiveAmount)
    }
    // Test for getCereal
    @Test
    fun `getCereal should throw if added amount of cereal is lower than 0`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.RICE, -1.1f)
        }
    }

    @Test
    fun `getCereal returns correct amount if taken amount is lower than stored`() {
        val storageAmount = 8f

        storage.addCereal(Cereal.RICE, storageAmount)

        val amount = 4.4f

        val takenAmount = storage.getCereal(Cereal.RICE, amount)

        assertEquals(amount, takenAmount)
    }

    @Test
    fun `getCereal returns correct amount if taken amount is 0f`() {
        val storageAmount = 8f

        storage.addCereal(Cereal.RICE, storageAmount)

        val amount = 0f

        val takenAmount = storage.getCereal(Cereal.RICE, amount)

        assertEquals(amount, takenAmount)
    }

    @Test
    fun `getCereal returns correct amount if taken amount is higher than stored`() {
        val storageAmount = 8f

        storage.addCereal(Cereal.RICE, storageAmount)

        val amount = 9.9f

        val takenAmount = storage.getCereal(Cereal.RICE, amount)

        assertEquals(storageAmount, takenAmount)
    }

    @Test
    fun `getCereal should throw exception if cereal container is not in storage`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getCereal(Cereal.RICE, 1f)
        }
    }

    // tests for remove container

    @Test
    fun `removeContainer returns true if it  removes hollow container`() {
        storage.addCereal(Cereal.RICE, 0f)

        val result = storage.removeContainer(Cereal.RICE)

        assertTrue(result)
    }

    @Test
    fun `removeContainer returns false if container is not removed`() {
        storage.addCereal(Cereal.RICE, 1f)


        val result = storage.removeContainer(Cereal.RICE)

        assertFalse(result)
    }

    @Test
    fun `removeContainer should throw exception if cereal container is not in storage`() {
        assertThrows(IllegalStateException::class.java) {
            storage.removeContainer(Cereal.RICE)
        }
    }


    // tests for getAmount

    @Test
    fun `getAmount should return the amount of cereal in storage`() {
        val amount = 5.5f
        storage.addCereal(Cereal.PEAS, amount)

        val resultAmount = storage.getAmount(Cereal.PEAS)

        assertEquals(amount, resultAmount)
    }

    @Test
    fun `getAmount should throw exception if cereal container is not in storage`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getAmount(Cereal.PEAS)
        }
    }

    // tests for getSpace

    @Test
    fun `getSpace should return remaining space in container`() {
        val amount = 5.5f
        storage.addCereal(Cereal.MILLET, amount)

        val space = storage.getSpace(Cereal.MILLET)

        assertEquals(containerCapacity - amount, space)
    }

    @Test
    fun `getSpace should throw exception if cereal container is not in storage`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getSpace(Cereal.MILLET)
        }

    }

    @Test
    fun `toString should return correct string representation`() {
        val cereal1 = Cereal.PEAS
        val cereal2 = Cereal.MILLET

        val amount1 = 5.4f
        val amount2 = 8.3f

        storage.addCereal(cereal1, amount1)
        storage.addCereal(cereal2, amount2)

        val expectedString = mapOf(
            cereal1 to amount1,
            cereal2 to amount2
        ).toString()

        val storageString = storage.toString()

        assertEquals(expectedString, storageString)

    }
}
