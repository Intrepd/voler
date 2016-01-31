package com.devineemail.voler;

import java.io.IOException;

import com.devineemail.util.HexUtils;
import com.devineemail.voler.io.MessageIO;
import com.devineemail.voler.model.AbstractModel.Listener;
import com.devineemail.voler.model.ServerAirConditionerStatusModel;
import com.devineemail.voler.model.ServerFurnaceStatusModel;
import com.devineemail.voler.model.ServerSAMModel;
import com.devineemail.voler.model.ServerTableMessageModel;
import com.devineemail.voler.model.ServerThermostatModel;

import jssc.SerialPortException;

public class Main
{
	public Main(final String deviceName) throws IOException
	{
		final MessageIO messageIO = new MessageIO(deviceName);

		final ServerTableMessageModel tableMessageModel = ServerTableMessageModel.getInstance();
		tableMessageModel.register(messageIO);
		final ServerSAMModel samModel = ServerSAMModel.getInstance();
		samModel.register(messageIO);
		final ServerThermostatModel thermostatModel = ServerThermostatModel.getInstance();
		thermostatModel.register(messageIO);
		thermostatModel.addListener(new Listener()
		{
			@Override
			public void modelChanged()
			{
				System.out.println("Indoor Temp: " + thermostatModel.getIndoorTemp() + " Outdoor Temp: " + thermostatModel.getOutdoorTemp() + " Humidity: "
						+ thermostatModel.getHumidity() + " Time: " + thermostatModel.getTime());
				System.out.println("Fan Mode: " + thermostatModel.getFanMode() + " " + "Cool Set: " + thermostatModel.getCoolSetPoint() + " Heat Set: "
						+ thermostatModel.getHeatSetPoint() + " On Hold: " + thermostatModel.isOnHold() + " Hold Duration: " + thermostatModel.getHoldDuration());
			}
		});
		thermostatModel.setRefreshInterval(10);
		final ServerFurnaceStatusModel furnaceStatusModel = ServerFurnaceStatusModel.getInstance();
		furnaceStatusModel.register(messageIO);
		furnaceStatusModel.addListener(new Listener()
		{
			@Override
			public void modelChanged()
			{
				System.out.println("Unknown: " + HexUtils.toHexString(furnaceStatusModel.getUnknown(), 4) + " Blower CFM: " + furnaceStatusModel.getBlowerCFM()
						+ " Blower RPM: " + furnaceStatusModel.getBlowerRPM());
			}
		});
		final ServerAirConditionerStatusModel acStatusModel = ServerAirConditionerStatusModel.getInstance();
		acStatusModel.register(messageIO);

		messageIO.start();
	}

	public static void main(final String args[]) throws IOException, SerialPortException
	{
		if (args.length < 1)
		{
			new Main(args[0]);
		}
		else
		{
			System.out.println("Usage: java -jar voler.jar <Serial Port Name>");
		}
	}
}
