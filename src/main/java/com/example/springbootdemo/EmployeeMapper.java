import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface EmployeeMapper {

    // 只要调用这个方法，MyBatis 就会自动去执行这句 SQL，并把结果打包成 Employee 的 List 集合！
    @Select("SELECT id, name, age, gender, entry_date FROM employee")
    List<Employee> getAllEmployees();
}
