package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)

    private fun randomCereal(): Cereal = Cereal.entries.toTypedArray().random()

    private fun randomFloat(min: Float = 0f, max: Float = 10f): Float {
        require(min <= max) { "Min must not be greater than max" }
        return Random.nextFloat() * (max - min) + min
    }

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(randomFloat(-10f, -1f), 10f)
        }
    }

    @Test
    fun `should throw if storageCapacity is lower than containerCapacity`() {
        val containerCapacity = randomFloat(5f, 10f)
        val storageCapacity = randomFloat(1f, 4f)
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(containerCapacity, storageCapacity)
        }
    }

    // Tests for addCereal

    @Test
    fun `addCereal should throw if added amount of cereal is lower than 0`() {
        val randomCereal = randomCereal()
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(randomCereal, randomFloat(-10f, -1f))
        }
    }

    @Test
    fun `addCereal should throw if no more containers can be placed in storage`() {
        storage.addCereal(randomCereal(), 10f)
        storage.addCereal(randomCereal(), 10f)

        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(randomCereal(), 10f)
        }
    }

    @Test
    fun `addCereal return 0f as excessive amount if container is added and containerCapacity is not exceeded`() {
        val randomCereal = randomCereal()
        val amount = randomFloat(1f, 9f)
        val excessiveAmount = storage.addCereal(randomCereal, amount)

        assertEquals(0f, excessiveAmount)
    }

    @Test
    fun `addCereal returns correct excessive amount if container is added and capacity is lower than added amount`() {
        val randomCereal = randomCereal()
        val amount = randomFloat(11f, 20f)
        val excessiveAmount = storage.addCereal(randomCereal, amount)

        assertEquals(amount - 10f, excessiveAmount)
    }

    @Test
    fun `addCereal returns 0f if container exist and capacity is not exceeded`() {
        val randomCereal = randomCereal()
        storage.addCereal(randomCereal, 8f)

        val excessiveAmount = storage.addCereal(randomCereal, 2f)

        assertEquals(0f, excessiveAmount)
    }

    @Test
    fun `addCereal returns correct excessive amount if container exist and capacity is exceeded`() {
        val randomCereal = randomCereal()
        val storageAmount = randomFloat(1f, 10f)

        storage.addCereal(randomCereal, storageAmount)

        val addedAmount = randomFloat(10f, 20f)

        val excessiveAmount = storage.addCereal(randomCereal, addedAmount)

        val expectedValue = storageAmount + addedAmount - 10f

        assertEquals(expectedValue, excessiveAmount)
    }
    // Test for getCereal
    @Test
    fun `getCereal should throw if added amount of cereal is lower than 0`() {
        val randomCereal = randomCereal()
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(randomCereal, randomFloat(-10f, -1f))
        }
    }

    @Test
    fun `getCereal returns correct amount if taken amount is lower than stored`() {
        val randomCereal = randomCereal()
        val storageAmount = randomFloat(1f, 10f)

        storage.addCereal(randomCereal, storageAmount)

        val amount = randomFloat(1f, storageAmount)

        val takenAmount = storage.getCereal(randomCereal, amount)

        assertEquals(amount, takenAmount)
    }

    @Test
    fun `getCereal returns correct amount if taken amount is higher than stored`() {
        val randomCereal = randomCereal()
        val storageAmount = randomFloat(1f, 10f)

        storage.addCereal(randomCereal, storageAmount)

        val amount = randomFloat(storageAmount, 10f)

        val takenAmount = storage.getCereal(randomCereal, amount)

        assertEquals(storageAmount, takenAmount)
    }

    @Test
    fun `getCereal should throw exception if cereal container is not in storage`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getCereal(randomCereal(), randomFloat(1f, 10f))
        }
    }

    // tests for remove container

    @Test
    fun `removeContainer returns true if it  removes hollow container`() {
        val randomCereal = randomCereal()
        storage.addCereal(randomCereal, 0f)

        val result = storage.removeContainer(randomCereal)

        assertTrue(result)
    }

    @Test
    fun `removeContainer returns false if container is not removed`() {
        val randomCereal = randomCereal()
        storage.addCereal(randomCereal, 1f)

        val result = storage.removeContainer(randomCereal)

        assertFalse(result)
    }

    @Test
    fun `removeContainer should throw exception if cereal container is not in storage`() {
        assertThrows(IllegalStateException::class.java) {
            storage.removeContainer(randomCereal())
        }
    }


    // tests for getAmount

    @Test
    fun `getAmount should return the amount of cereal in storage`() {
        val randomCereal = randomCereal()
        val amount = randomFloat(1f, 10f)
        storage.addCereal(randomCereal, amount)

        val resultAmount = storage.getAmount(randomCereal)

        assertEquals(amount, resultAmount)
    }

    @Test
    fun `getAmount should throw exception if cereal container is not in storage`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getAmount(randomCereal())
        }
    }

    // tests for getSpace

    @Test
    fun `getSpace should return remaining space in container`() {
        val randomCereal = randomCereal()
        val amount = randomFloat(1f, 10f)
        storage.addCereal(randomCereal, amount)

        val space = storage.getSpace(randomCereal)

        assertEquals(10f - amount, space)
    }

    @Test
    fun `getSpace should throw exception if cereal container is not in storage`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getSpace(randomCereal())
        }

    }

    @Test
    fun `toString should return correct string representation`() {
        val cereal1 = Cereal.PEAS
        val cereal2 = Cereal.MILLET

        val amount1 = randomFloat(1f, 10f)
        val amount2 = randomFloat(1f, 10f)

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
