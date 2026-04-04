# Unit 2: Advanced Object-Oriented Programming

A deep dive into the four pillars of OOP (Encapsulation, Polymorphism, Inheritance, and Abstraction) and the implementation of complex logic using classes, interfaces, and data structures.

## Index

- [1. Introduction to the Core Paradigm](#1-introduction-to-the-core-paradigm)
- [2. Instantiation and Lifecycle: Constructors and Destructors](#2-instantiation-and-lifecycle-constructors-and-destructors)
- [3. The Principle of Inheritance](#3-the-principle-of-inheritance)
- [4. Polymorphism and Method Overloading](#4-polymorphism-and-method-overloading)
- [5. Encapsulation and Access Control](#5-encapsulation-and-access-control)
- [6. Advanced Structural Elements](#6-advanced-structural-elements)
- [7. Comparative Technical Reference Table](#7-comparative-technical-reference-table)
- [8. Exercises](#8-exercises)

### 1. Introduction to the Core Paradigm

Object-Oriented Programming (OOP) is a development paradigm where programs are conceptualized as sets of interacting entities. According to the "Sesión 02" curriculum, the structure is defined by three pillars:

* **Object**: The representation of the state (data) and behavior (methods) of a real or abstract entity.
* **Interaction Model**: Programs consist of objects that communicate via messages. An object responds to a message by executing a specific method.
* **Class**: A data type acting as a blueprint. It describes the shared behavior and data types (though not the specific values) for a set of instances.

> **Architectural Note on Consistent State:** A fundamental architectural requirement is that classes must be implemented so that their instances always maintain a "consistent state." This means the object's data must remain valid throughout its lifecycle, a responsibility primarily managed during instantiation.

### 2. Instantiation and Lifecycle: Constructors and Destructors

Managing the lifecycle of an object—from its birth in memory to its eventual cleanup—is critical for performance and reliability.

#### Constructors
Constructors are specialized methods used to build or instantiate a class. They are the primary mechanism for ensuring "Consistent State" from the moment of creation. While a class can have multiple constructors (overloading) to satisfy various initialization requirements, the syntax remains nearly identical between Java and C#.

| Feature | Java (Estudiante) | C# (Estudiante) |
|---|---|---|
| **Logic Parity** | `public Estudiante(String nombre, double CRAEST) {`<br>`  this.nombre = nombre;`<br>`  this.CRAEST = CRAEST;`<br>`}` | `public Estudiante(string nombre, double CRAEST) {`<br>`  this.nombre = nombre;`<br>`  this.CRAEST = CRAEST;`<br>`}` |

#### Destructors and Memory Management
In modern managed languages like Java and C#, manual destruction is generally unnecessary. Both platforms utilize a Garbage Collector (GC) that automatically reclaims memory from objects that are no longer referenced.

> **Professional Note on Destruction Syntax:** While the curriculum provides the following syntax for destruction, a Senior Architect must note that in modern Java, `finalize()` is deprecated and its execution is non-deterministic. In C#, the tilde `~` syntax defines a Finalizer, which should also be used sparingly.

* **Java (Legacy):** `public void finalize() { System.out.println("El objeto se esta destruyendo"); }`
* **C#:** `~Estudiante() { System.Console.WriteLine("Se esta destruyendo un objeto"); }`

### 3. The Principle of Inheritance

Inheritance allows developers to create new classes based on existing ones, facilitating the reuse and extension of code.

* **Hierarchy:** The source class is the **Base Class**, and the inheriting class is the **Derived Class**.
* **Reusability:** Derived classes inherit all members of the base. For example, the classes `ProfesorTiempoCompleto` and `ProfesorTiempoParcial` both inherit the attributes `nombre` and `direccion` from the `Profesor` base class.
* **Constraints:** Both languages follow single inheritance (one direct base class), though inheritance is transitive across multiple levels.
* **Syntax:** Java uses the `extends` keyword, while C# uses the colon `:` operator.

### 4. Polymorphism and Method Overloading

The source explicitly identifies three types of polymorphism: (1) Subtype, (2) Overloading (Sobrecarga), and (3) Parametric.

#### Subtype Polymorphism
This allows a base class reference to hold a derived class object and trigger behavior specific to that derived type. This is typically achieved via virtual or abstract methods.

**Implementation Examples:** The following code demonstrates a base class variable (`Figura`) holding instances of derived classes (`Triangulo`, `Cuadrado`) and executing the correct overridden method.

**Java (`Principal.java`):**
```java
public class Principal {
    public static void main(String[] args) {
        Figura t1 = new Triangulo(10, 20); // Base reference to Derived instance
        Figura c1 = new Cuadrado(10, 20);
        
        t1.calcularArea(); // Executes Triangulo's logic
        c1.calcularArea(); // Executes Cuadrado's logic
    }
}
```

**C# (`Program.cs`):**
```csharp
class Program {
    static void Main(string[] args) {
        Figura t1 = new Triangulo(10, 20); // Base reference to Derived instance
        Figura c1 = new Cuadrado(10, 20);
        
        t1.CalcularArea(); // Executes Triangulo's logic
        c1.CalcularArea(); // Executes Cuadrado's logic
    }
}
```

*Note:* Java uses the `@Override` annotation for clarity and compiler safety (though it is technically optional), while C# strictly requires the `virtual` keyword in the base class and `override` in the derived class.

**Java Override Syntax:**
```java
class Figura {
    // Can define base logic since this method is not abstract
    public void calcularArea() { /* base logic */ }
}

class Triangulo extends Figura {
    @Override // Optional but recommended
    public void calcularArea() { /* derived logic */ }
}
```

**C# Override Syntax:**
```csharp
class Figura {
    // Can define base logic since this method is not abstract
    public virtual void CalcularArea() { /* base logic */ }
}

class Triangulo : Figura {
    public override void CalcularArea() { /* derived logic */ }
}
```

#### Method Overloading (Sobrecarga)
Overloading allows multiple methods or constructors to share the same identifier, provided they are distinguished by the number or type of their arguments.

* **Constructor Overloading:** The `Persona` class demonstrates this by offering one constructor for `nombre` and another for both `nombre` and `apellidoPaterno`.
* **Method Overloading:** The `sumar` method is overloaded to handle two integers, three integers, or two doubles.

### 5. Encapsulation and Access Control

Encapsulation is the practice of restricting direct access to an object's internal data to maintain the "Consistent State" mentioned in Section 1.

* **Private Modifiers:** Fields such as `nombre` and `CRAEST` are marked `private` to prevent external corruption.
* **Indirect Access:** All interactions with the object's state must occur through `public` methods (interfaces), ensuring that any changes adhere to the class's internal logic.

### 6. Advanced Structural Elements

The curriculum identifies the following topics as essential for programming complex relations between classes:

* **Abstract Classes and Interfaces:** For defining contracts and incomplete types.
* **Static Members:** For class-level rather than instance-level data.
* **Nested Classes (Anidada) and Enumerations:** For internal grouping and fixed sets of constants.
* **Indexers (Indizadores):** Specifically used in C# for array-like access to objects.
* **Type Checking:** The use of `instanceof` (Java) and `is` (C#) to verify object types at runtime.

### 7. Comparative Technical Reference Table

| Feature | Java Implementation | C# Implementation |
|---|---|---|
| **Inheritance Syntax** | `extends` keyword | `:` (Colon) operator |
| **Destructor Syntax** | `public void finalize()` (Deprecated) | `~ClassName()` (Finalizer) |
| **Console Output** | `System.out.println()` | `System.Console.WriteLine()` |
| **String Data Type** | `String` (Class) | `string` (Alias/Keyword) |
| **Method Overriding** | `@Override` annotation | `virtual` (Base) / `override` (Derived) |
| **Type Comparison** | `instanceof` | `is` |

### 8. Exercises

The full descriptions for the following exercises can be found on the **Paideia** platform:

1. **The Figures Problem** *(Found in: 2026-1 PROGRAMACIÓN 3 (1INF30) > Ejercicios - 0682 - Corcuera)*
   - **Solution:** [`figures_problem/`](./figures_problem/)
2. **EQUIPU** *(Found in the Second Week section)*
   - **Solution:** [`equipu_problem/`](./equipu_problem/)
3. **Lab 01 - 2026-1** *(Found in the First Week lab 01 section)*
   - **Solution:** [`202601_lab01_solution/`](./202601_lab01_solution/)

> **Note on Execution:** To compile the Java solutions, navigate using your terminal to the respective folder and use the `javac` command on the source files (e.g., `javac *.java`). Then, to run the application, use the `java` command followed by the exact name of the class containing the `main` method (e.g., `java Principal`).
