package ru.webrelab.kie.cerealstorage

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(cereal: Cereal, amount: Float): Float {

        require(amount >= 0) {
            throw IllegalArgumentException("Количество крупы не может быть меньше нуля")
        }

        if (storage.size * containerCapacity >= storageCapacity) {
            throw IllegalStateException("Хранилище не позволяет разместить ещё один контейнер для новой крупы")
        }


        val currentAmount = storage.getOrDefault(cereal, 0f)
        val newAmount = currentAmount + amount

        val excessiveAmount = if (newAmount > containerCapacity) {
            newAmount - containerCapacity
        } else {
            0f
        }

        storage[cereal] = minOf(newAmount, containerCapacity)

        return excessiveAmount

    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {

        require(amount >= 0) {
            throw IllegalArgumentException("Количество крупы не может быть меньше нуля")
        }

        // В интерфейсе ничего нет про кейс, когда getCereal,removeContainer, getAnound и getSpace обращаются
        // к несуществубщему контейнеру. Добавил ошибку на этот случай

        if (!storage.containsKey(cereal)) {
            throw IllegalStateException("В хранилище нет контейнера с выбранной крупой")
        }

        val currentAmount = storage.getOrDefault(cereal, 0f)

        storage[cereal] = if (amount >= currentAmount) {
            0f
        } else {
            currentAmount - amount
        }

        return minOf(currentAmount, amount)

    }

    override fun removeContainer(cereal: Cereal): Boolean {


        if (!storage.containsKey(cereal)) {
            throw IllegalStateException("В хранилище нет контейнера с выбранной крупой")
        }

        if (storage[cereal] == 0f) {
            storage.remove(cereal)
        }

        return !storage.containsKey(cereal)
    }

    override fun getAmount(cereal: Cereal): Float {

        if (!storage.containsKey(cereal)) {
            throw IllegalStateException("В хранилище нет контейнера с выбранной крупой")
        }

        return storage.getOrDefault(cereal, 0f)
    }

    override fun getSpace(cereal: Cereal): Float {

        if (!storage.containsKey(cereal)) {
            throw IllegalStateException("В хранилище нет контейнера с выбранной крупой")
        }

        val currentAmount = storage.getOrDefault(cereal, 0f)

        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        return storage.toString()
    }

}
