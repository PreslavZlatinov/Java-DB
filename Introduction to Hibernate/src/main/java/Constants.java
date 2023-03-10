enum Constants {
    ;

    static final String ADDRESS_FOR_ADDING = "Vitoshka 15";

    static final String PERSISTENCE_UNIT_NAME = "soft_uni";

    enum Queries {
        ;

        static final String UPDATE_ALL_TOWNS_WITH_LENGTH_NAME_MORE_THAN_5 =
                "UPDATE Town t SET t.name = UPPER(t.name) WHERE LENGTH(t.name) <= 5";

        static final String UPDATE_EMPLOYEE_ADDRESS_BY_LAST_NAME =
                "UPDATE Employee e set e.address = :newAddress WHERE e.lastName = :ln";

        static final String GET_ALL_ADDRESSES_ORDER_BY_EMPLOYEE_SIZE_DESC =
                "select a from Address a order by a.employees.size desc";

        static final String GET_ALL_TOWNS = "Select t from Town t";

        static final String GET_TOWN_BY_TOWN_NAME = "SELECT t FROM Town AS t WHERE t.name = :tName";

        static final String GET_ALL_EMPLOYEES_BY_DEPARTMENT_ORDER_BY_SALARY =
                "select e from Employee e where e.department.name = :dp order by e.salary asc, e.id";

        static final String GET_COUNT_OF_EMPLOYEES_BY_FULL_NAME =
                "SELECT count(e) FROM Employee e WHERE e.firstName = :fn AND e.lastName = :ln";

        static final String GET_EMPLOYEES_FIRST_NAMES_WITH_SALARY_MORE_THAN_50000 =
                "Select e.firstName from Employee e WHERE e.salary > 50000";

        static final String GET_EMPLOYEE_BY_ID = "SELECT e FROM Employee e WHERE e.id = :id";

        static final String UPDATE_EMPLOYEE_SALARIES_BY_12_PERCENTS =
                "UPDATE Employee e set e.salary = e.salary * 1.12 WHERE e.department.id in (1, 2, 14,3)";

        static final String GET_UPDATED_EMPLOYEES_IN_DEPARTMENTS =
                "Select e From Employee as e " +
                        "WHERE e.department.name in ('Information Services', 'Marketing', 'Tool Design', 'Engineering')";

        static final String GET_PROJECTS_ORDER_BY_START_DATA_DESC =
                "SELECT p FROM Project AS p ORDER BY p.startDate DESC";

        static final String GET_EMPLOYEES_WITH_FIRST_NAME_LIKE =
                "SELECT e FROM Employee AS e WHERE e.firstName LIKE :fn";

        static final String GET_ALL_ADDRESSES_BY_TOWN_NAME = "SELECT a FROM Address AS a WHERE a.town.name = :tName";

        static final String GET_EMPLOYEE_MAX_SALARY_BY_DEPARTMENT_WHERE_SALARY_BETWEEN_30000_AND_70000_ORDER_BY_SALARY =
                "SELECT e FROM  Employee AS e " +
                        "WHERE e.salary NOT BETWEEN 30000 AND 70000 " +
                        "GROUP BY e.department " +
                        "ORDER BY e.salary DESC";
    }

    enum PlaceHolders {
        ;

        static final String FIRST_NAME = "fn";

        static final String LAST_NAME = "ln";

        static final String TOWN_NAME = "tName";

        static final String ID = "id";

        static final String DEPARTMENT_NAME = "dp";

        static final String ADDRESS_PLACEHOLDER = "newAddress";
    }

    enum Departments {
        ;

        static final String RESEARCH_AND_DEV = "Research and Development";

    }
}