# Unit 3: Software Modularity & Reusability

Focuses on organizing code through packages and namespaces to create reusable components (libraries) that can be shared across different software projects.

## Index

- [1. Reusable Components and Libraries (Java & C#)](#1-reusable-components-and-libraries-java--c)
- [2. Java Packages and Libraries](#2-java-packages-and-libraries)
- [3. C# / .NET Libraries and DLLs](#3-c--net-libraries-and-dlls)
- [4. Examples](#4-examples)

## 1. Reusable Components and Libraries (Java & C#)

### Overview
A reusable component is a software piece designed to be used multiple times across different programs or modules without needing to be rewritten. Depending on the language, these can take the form of classes, packages, libraries (JARs), or DLL assemblies.

### Key Characteristics & Principles
*   **Characteristics:** Reusable components must be modular, independent, flexible, encapsulated, maintainable, and well-documented.
*   **Design Principles:** They should be built with high cohesion (doing one task well), low coupling (minimal dependencies), encapsulation, abstraction, and generalization.

## 2. Java Packages and Libraries

### Packages
Packages are hierarchical containers used to organize related classes that share a specific functionality or purpose. 

### Libraries (JAR Files)
A library in Java is an archive (`.jar`) containing compiled, reusable `.class` files. To use a library, it must be included in the Java `CLASSPATH`.

*   **Creating a JAR:** Use the command `jar cvf "package_name.jar" *.class`.
*   **Executable JARs:** To make a JAR executable, you must modify the `Manifest` file to explicitly state the `Main-Class` and the `Class-Path` variables.
*   **Running an Executable JAR:** Run using the command `java -jar <filename>.jar`.

## 3. C# / .NET Libraries and DLLs

### DLLs (Dynamic Link Libraries)
In .NET, a DLL is the physical compiled file that represents a library. While console applications generate an executable (`.exe`) launcher alongside a DLL, Class Library projects solely generate a DLL for reuse.

### Namespaces and Access Modifiers
*   **Namespaces:** C# uses `namespace` to logically organize code, which is conceptually similar to Java packages but does not require a matching physical folder structure. You access elements from a namespace using the `using` keyword.
*   **Access Modifiers:** The default class modifier in C# is `internal` (accessible only within the same project/assembly). To make a class accessible to other projects, it must be marked as `public`. *(Note: In Java, access is determined by packages, while in C#, it is determined by the assembly)*.

### Essential .NET CLI Commands
*   **Create a Console App:** `dotnet new console -n <ProjectName>`
*   **Create a Class Library:** `dotnet new classlib -n <LibraryName>`
*   **Compile a Project:** `dotnet build`
*   **Run a Project:** `dotnet run`

### Consuming and Distributing Libraries
1.  **Project Reference:** Use `dotnet add reference <path_to_csproj>` if you have the library's source code. 
2.  **Manual Reference:** Manually edit the `.csproj` file to include the path to an external `.dll` file.
3.  **NuGet Distribution:** Libraries can be packaged into `.nupkg` files using `dotnet pack` and shared/consumed using `dotnet package add <package_name>`.

## 4. Examples

The following directories contain practical examples related to the concepts discussed in this unit:

1. **Full Classname Usage**
   - **Source:** [`full-classname/`](./full-classname/)
   - **Commands to build the project files:**
     ```bash
     # 1. Build the library JAR using the contents of the 'temporal' folder
     jar cvf my-library.jar -C temporal .
     
     # 2. Build the executable JAR using the 'principal' package and the MANIFEST.MF
     jar cvfm my-jar-executable.jar MANIFEST.MF principal/*.class
     
     # 3. Run the executable JAR
     java -jar my-jar-executable.jar
     ```
2. **Basic Console Application**
   - **Source:** [`console-app/`](./console-app/)
   - **Commands to create and launch the app:**
     ```bash
     # 1. Create a new console application named 'console-app'
     dotnet new console -n console-app
     
     # 2. Navigate to the generated directory
     cd console-app
     
     # 3. Launch the application
     dotnet run
     ```
3. **Matematicas Library**
   - **Source:** [`Matematicas/`](./Matematicas/)
   - **Commands to create and build the library:**
     ```bash
     # Create a new C# class library named 'Matematicas'
     dotnet new classlib -n Matematicas
     
     # Navigate to the generated library project
     cd Matematicas
     
     # Build the C# class library (generates bin/Debug/net10.0/Matematicas.dll)
     dotnet build
     ```
4. **Consuming an External DLL**
   - **Source:** [`PrincipalConsumeDLL/`](./PrincipalConsumeDLL/)
   - **Configuration (.csproj):**
     To manually reference the external DLL, the project file must include the following metadata pointing to its physical location:
     ```xml
     <ItemGroup>
       <Reference Include="Matematicas">
         <HintPath>libs/Matematicas.dll</HintPath>
       </Reference>
     </ItemGroup>
     ```
   - **Commands to run the consuming application:**
     ```bash
     # Navigate to this project
     cd PrincipalConsumeDLL
     
     # Important: You must first copy the generated Matematicas.dll into the 'libs' folder
     # cp ../Matematicas/bin/Debug/net10.0/Matematicas.dll ./libs/
     
     # Run the application (it uses the DLL path defined in its .csproj HintPath)
     dotnet run
     ```
5. **Consuming a Project Reference**
   - **Source:** [`PrincipalConsumeProject/`](./PrincipalConsumeProject/)
   - **Adding the Reference:** Instead of referencing a compiled binary, this approach links directly to the source project. The .NET engine will automatically compile dependencies when needed. There are **two equivalent ways** to establish this reference:
     
     **Method 1: Manual configuration (.csproj)**
     You can manually edit the `.csproj` file to include a `ProjectReference` pointing to the physical file path:
     ```xml
     <ItemGroup>
       <ProjectReference Include="..\Matematicas\Matematicas.csproj" />
     </ItemGroup>
     ```
     
     **Method 2: Using the .NET CLI**
     Alternatively, you can let the CLI automatically update the `.csproj` file for you by running the following command inside the project directory:
     ```bash
     dotnet add reference ../Matematicas/Matematicas.csproj
     ```
   - **Execution Setup:**
     ```bash
     # Navigate to the project directory
     cd PrincipalConsumeProject
     
     # Run the application (this automatically compiles the Matematicas dependency beforehand)
     dotnet run
     ```
