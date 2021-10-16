Summary: Jyske Bank Cluster Configurator
Name: jb-cluster-configurator
Version:  %{_version}
Release: %{_release}
URL:     http://jyskebank.dk	
License: GPL
Group: Development/Tools
BuildRoot: %{_tmppath}/%{name}-root
AutoReqProv: no
Requires: libGL
Requires: gtk3
Requires: alsa-lib
Requires: ffmpeg-libs
Packager: henrik lund <henriklund@jyskebank.dk>

%define _build_id_links none

%description
Jyske Bank Cluster Configurator. Allows cluster configuration on OpenShift platform.

%pre

%install
/bin/rm -rf ${RPM_BUILD_ROOT}

/usr/bin/install -D /github/home/rpmbuild/SOURCES/cluster-configurator ${RPM_BUILD_ROOT}/usr/bin/cluster-configurator
/usr/bin/install -D /github/home/rpmbuild/SOURCES/cluster-configurator.desktop ${RPM_BUILD_ROOT}/usr/share/applications/cluster-configurator.desktop
/usr/bin/install -D /github/home/rpmbuild/SOURCES/cluster-configurator.png ${RPM_BUILD_ROOT}/usr/share/jb/icons/cluster-configurator.png

exit 0

%clean 
/bin/rm -rf ${RPM_BUILD_ROOT} 

%post

%preun

%postun


%files
%defattr(644,root,root)
%attr(755, root, root) /usr/bin/cluster-configurator
/usr/share/applications/cluster-configurator.desktop
/usr/share/jb/icons/cluster-configurator.png


%changelog
* Tue May 11 2021 Henrik Lund <henriklund@jyskebank.dk> - 1.0-0
- New package.
