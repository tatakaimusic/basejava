import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        for(int i = 0; i< storage.length; i++){
            if(storage[i] != null){
                storage[i] = null;
            }else{
                break;
            }
        }
    }

    void save(Resume r) {
        for(int i = 0; i< storage.length; i++){
            if(storage[i] == null){
                storage[i] = r;
                break;
            }
        }
    }

    Resume get(String uuid) {
        Resume id = null;
        for(Resume resume : storage ){
            if(resume.uuid.equals(uuid)){
               id = resume;
               break;
            }
        }
        return id;
    }

    void delete(String uuid) {
        List<Resume> listStorage = new ArrayList<>();
        for(int i = 0; i<storage.length; i++){
            if(storage[i] != null) {
                listStorage.add(storage[i]);
            }else{
                break;
            }
        }
        clear();
        for(int i = 0; i<listStorage.size(); i++){
            if(listStorage.get(i).uuid.equals(uuid)){
                listStorage.remove(i);
                break;
            }
        }

        for(int i = 0; i<listStorage.size(); i++){
            storage[i] = listStorage.get(i);
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        int size = 0;
        for(int i = 0; i<storage.length; i++){
            if(storage[i] != null){
                size++;
            }else{
                break;
            }
        }
        Resume[] temporaryStorage = new Resume[size];
        for(int i = 0; i<storage.length; i++){
            if(storage[i] != null){
                temporaryStorage[i] = storage[i];
            }else{
                break;
            }
        }

        return temporaryStorage;
    }

    int size() {
        int size = 0;
        for(int i = 0; i< storage.length; i++){
            if(storage[i] != null)
                size++;
        }
        return size;
    }
}
