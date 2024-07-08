package SerializationDemo;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PojoClass implements Serializable {

    private int empId;
    private String empName;
    private double empSal;
    private String empDepartment;


}
