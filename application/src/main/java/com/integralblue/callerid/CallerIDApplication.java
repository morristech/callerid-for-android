package com.integralblue.callerid;

import java.util.List;

import org.apache.http.client.HttpClient;

import roboguice.application.RoboApplication;
import roboguice.config.AbstractAndroidModule;
import roboguice.inject.SharedPreferencesName;
import roboguice.inject.SystemServiceProvider;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.google.inject.Module;
import com.google.inject.Scopes;
import com.integralblue.callerid.contacts.ContactsHelper;
import com.integralblue.callerid.geocoder.Geocoder;
import com.integralblue.callerid.inject.ContactsHelperProvider;
import com.integralblue.callerid.inject.GeocoderHelperProvider;
import com.integralblue.callerid.inject.HttpClientProvider;

public class CallerIDApplication extends RoboApplication {
    protected void addApplicationModules(List<Module> modules) {
    	super.addApplicationModules(modules);
    	modules.add(new AbstractAndroidModule() {
			@Override
			protected void configure() {
				
				//https://code.google.com/p/roboguice/issues/detail?id=77 fixed in as yet unreleased Roboguice 1.2
				bind(TelephonyManager.class).toProvider( new SystemServiceProvider<TelephonyManager>(Context.TELEPHONY_SERVICE));
				
				bindConstant().annotatedWith(SharedPreferencesName.class).to(this.getClass().getPackage().getName() + "_preferences"); 
				bind(ContactsHelper.class).toProvider(ContactsHelperProvider.class).in(Scopes.SINGLETON);
				bind(CallerIDLookup.class).to(HttpCallerIDLookup.class).in(Scopes.SINGLETON);
				bind(Geocoder.class).toProvider(GeocoderHelperProvider.class).in(Scopes.SINGLETON);
				bind(HttpClient.class).toProvider(HttpClientProvider.class).in(Scopes.SINGLETON);
			}
    	});
    }
}