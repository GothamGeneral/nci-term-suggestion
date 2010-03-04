@echo off
@rem ******************************************
@rem **** Command file to invoke build.xml ****
@rem ******************************************
setlocal
set DEVPROPFILE=C:\NCI-Projects\nci-termform-properties\properties\dev-upgrade.properties
set QAPROPFILE=C:\NCI-Projects\nci-termform-properties\properties\qa-upgrade.properties
set DATAQAPROPFILE=C:\NCI-Projects\nci-termform-properties\properties\data-qa-upgrade.properties
cls
if "%1" == "" (
    echo.
    echo Available targets are:
    echo.
    echo   clean        -- Remove classes directory for clean build
    echo   all          -- Normal build of application
    echo   install      -- Builds, installs JBoss locally
    echo   upgrade      -- Build and upgrade application
    echo   dev          -- Builds, upgrades JBoss on DEV
    echo   qa           -- Builds, upgrades JBoss on QA
    echo   data-qa      -- Builds, upgrades JBoss on Data QA
    echo   deploy       -- Redeploy application
    goto DONE
)
if "%1" == "all" (
    ant build:all
    goto DONE
)
if "%1" == "install" (
    ant deploy:local:install
    goto DONE
)
if "%1" == "upgrade" (
    ant deploy:local:upgrade
    goto DONE
)
if "%1" == "deploy" (
    ant deploy:hot
    goto DONE
)
if "%1" == "clean" (
    ant clean
    if exist ..\target\*.* (
       rmdir /Q /S ..\target
    )
    goto DONE
)
if "%1" == "dev" (
    ant -Dproperties.file=%DEVPROPFILE% deploy:remote:upgrade
    goto DONE
)
if "%1" == "qa" (
    ant -Dproperties.file=%QAPROPFILE% deploy:remote:upgrade
    goto DONE
)
if "%1" == "data-qa" (
    ant -Dproperties.file=%DATAQAPROPFILE% -Danthill.build.tag_built=desktop deploy:remote:upgrade
    goto DONE
)
:DONE
endlocal