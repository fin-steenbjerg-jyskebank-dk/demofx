Summary: Stonemountain DemoFX
Name: demofx
Version: 50
Release: 0
URL:     http://jyskebank.dk	
License: GPL
Group: Development/Tools
BuildRoot: %{_tmppath}/%{name}-root
AutoReqProv: no
Requires: libGL
Requires: gtk3
Requires: alsa-lib
Requires: ffmpeg-libs
Packager: Fin Steenbjerg <fin.steenbjerg@gmail.com>

%define _build_id_links none

%description
Stonemountain DemoFX. Demonstrates JavaFX.

%pre

%install
/bin/rm -rf ${RPM_BUILD_ROOT}

/usr/bin/install -D ${RPM_SOURCE_DIR}/demofx ${RPM_BUILD_ROOT}/usr/bin/demofx
/usr/bin/install -D ${RPM_SOURCE_DIR}/demofx.desktop ${RPM_BUILD_ROOT}/usr/share/applications/demofx.desktop
/usr/bin/install -D ${RPM_SOURCE_DIR}/demofx.png ${RPM_BUILD_ROOT}/usr/share/stonemountain/icons/demofx.png

exit 0

%clean 
/bin/rm -rf ${RPM_BUILD_ROOT} 

%post

%preun

%postun


%files
%defattr(644,root,root)
%attr(755, root, root) /usr/bin/demofx
/usr/share/applications/demofx.desktop
/usr/share/stonemountain/icons/demofx.png


%changelog
- New package.
