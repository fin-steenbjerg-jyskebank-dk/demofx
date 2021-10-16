Summary: JavaFX demonstration
Name: demofx
Version:  %{_version}
Release: %{_release}
URL:     https://stonemountain.dk	
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
JavaFX demonstration application.

%pre

%install
/bin/rm -rf ${RPM_BUILD_ROOT}

/usr/bin/install -D /github/home/rpmbuild/SOURCES/demofx ${RPM_BUILD_ROOT}/usr/bin/demofx
/usr/bin/install -D /github/home/rpmbuild/SOURCES/demofx.desktop ${RPM_BUILD_ROOT}/usr/share/applications/demofx.desktop
/usr/bin/install -D /github/home/rpmbuild/SOURCES/demofx.png ${RPM_BUILD_ROOT}/usr/share/stonemountain/icons/demofx.png

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
* Sat Oct 16 2021 Fin Steenbjerg <fin.steenbjerg@gmail.com> - 1.0-0
- New package.
