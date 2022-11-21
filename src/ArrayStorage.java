
/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int size = 0;
    Resume[] storage = new Resume[10000];

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    void save(Resume r) {
        storage[size] = r;
        size++;
    }

    Resume get(String uuid) {
        for (Resume resume : storage) {
            if (resume.uuid.equals(uuid)) {
                return resume;
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                storage[i] = null;
                int index = i;
                if (i != size - 1) {
                    for (int j = index; j < size - 1; j++) {
                        storage[j] = storage[j + 1];
                    }
                }
            }
        }
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] temporaryStorage = new Resume[size];
        for (int i = 0; i < size; i++) {
            temporaryStorage[i] = storage[i];
        }
        return temporaryStorage;
    }

    int size() {
        return size;
    }
}
