
/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int size = 0;
    Resume[] storage = new Resume[10000];

    void clear() {
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] != null) {
                storage[i] = null;
            } else {
                break;
            }
        }
        size = 0;
    }

    void save(Resume r) {
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] == null) {
                storage[i] = r;
                break;
            }
        }
        size++;
    }

    Resume get(String uuid) {
        Resume id = null;
        for (Resume resume : storage) {
            if (resume.uuid.equals(uuid)) {
                id = resume;
                break;
            }
        }
        return id;
    }

    void delete(String uuid) {
        int index = 0;
        for (int i = 0; i < storage.length; i++) {
            if (storage[i].uuid.equals(uuid)) {
                storage[i] = null;
                index = i;
                break;
            }
        }
        for (int i = index; i < storage.length; i++) {
            if (storage[i + 1] != null) {
                storage[i] = storage[i + 1];
                storage[i + 1] = null;
            } else {
                break;
            }
        }

        size--;

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        int size = 0;
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] != null) {
                size++;
            } else {
                break;
            }
        }
        Resume[] temporaryStorage = new Resume[size];
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] != null) {
                temporaryStorage[i] = storage[i];
            } else {
                break;
            }
        }
        return temporaryStorage;
    }

    int size() {
        return size;
    }
}
