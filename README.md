1. Download project
2. Make sure you have Windows SDK 7.1 and VC++ compilers installed.Those are prerequisite for graal vm native image.https://www.mathworks.com/matlabcentral/answers/101105-how-do-i-install-microsoft-windows-sdk-7-1
3. Open Windows SDK 7.1 Command Prompt
4. Go to boot_2.3.1_tomcat-1 directory and type compile.bat
5. Once tomcat starts successfully in new window hit ctrl+c to terminate the tomcat server gracefully.
6. Native image should be created inside build/native-image
7. Run the native image by running the boot-2.3.1-agent.exe
8. Hit the REST end point http://localhost:8080/greeting
9. Native image that I created is added into native-image folder. That can be executed directly.
