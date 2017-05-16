package com.electrocomponents.executor.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.executor.EcCommand;
import com.electrocomponents.executor.EcCommandSpecification;
import com.electrocomponents.model.domain.Locale;

/**
 * A Helper Class to provide common methods to create and manipulate {@link EcCommand} Objects.
 * @author Bhavesh Kavia
 */
public final class TestCommandHelper {
    /** The Locale for the EcCommand. */
    public static final Locale LOCALE_UK = new Locale("uk");

    /** A commons logger. */
    private static final Log LOG = LogFactory.getLog(TestCommandHelper.class);

    /** The Type of test EcCommand to be created. */
    public enum CommandType {
        /** Enum to represent TestAVPCommand. */
        TEST_AVP
        /** Enum to represent PricingCommand. */
        // PRICING,
        /** Enum to represent AvailabilityCommand. */
        // AVAILABILITY
    }

    /** Private Constructor. */
    private TestCommandHelper() {

    }

    /**
     * Create a EcCommand.
     * @param type - The Command type
     * @param id - An id to identify the command
     * @param config - Any Configuration needed for the command specification
     * @return the EcCommand created
     */
    public static EcCommand createCommand(final CommandType type, final String id, final Map<String, Object> config) {
        EcCommandSpecification spec = createCommandSpecification(type, id, config);
        EcCommand command = spec.buildCommand();
        return command;
    }

    /**
     * Create a List of Commands.
     * @param noOfCommandsToCreate - The Number of Commands to create.
     * @param type - The Command type
     * @param id - An id to identify the command
     * @param config - Any Configuration needed for the command specification
     * @return A List of Commands
     */
    public static List<EcCommand> createCommands(final int noOfCommandsToCreate, final CommandType type, final String id,
            final Map<String, Object> config) {
        List<EcCommand> commands = new ArrayList<EcCommand>(noOfCommandsToCreate);
        final String tempId = id + "_";
        for (int i = 1; i <= noOfCommandsToCreate; i++) {
            EcCommandSpecification spec = createCommandSpecification(type, tempId + i, config);
            EcCommand command = spec.buildCommand();
            commands.add(command);
        }
        return commands;
    }

    /**
     * Gets the EcCommand from the {@link Future}.
     * @param future - The {@link Future}
     * @return the {@link EcCommand}
     */
    public static EcCommand getCommand(final Future<EcCommand> future) {
        EcCommand ecCommand = null;
        try {
            ecCommand = future.get();
        } catch (InterruptedException e) {
            LOG.error("An Interrupted exception occurred.", e);
        } catch (ExecutionException e) {
            LOG.error("An ExecutionException occurred.", e);
        }
        return ecCommand;
    }

    /**
     * Creates a List of Command Specifications.
     * @param noToCreate - The Number to create
     * @param type - The Command Type
     * @param id - An id to identify the specification
     * @param config - Any Configuration needed for the command specification
     * @return List of {@link EcCommandSpecification}s
     */
    public static List<EcCommandSpecification> createCommandSpecifications(final int noToCreate, final CommandType type, final String id,
            final Map<String, Object> config) {
        List<EcCommandSpecification> specs = new ArrayList<EcCommandSpecification>(noToCreate);
        final String tempId = id + "_";
        for (int i = 1; i <= noToCreate; i++) {
            EcCommandSpecification spec = createCommandSpecification(type, tempId + i, config);
            specs.add(spec);
        }
        return specs;
    }

    /**
     * @param type - The Command type
     * @param id - An id to identify the specification
     * @param config - Any Configuration needed for the command specification
     * @return The {@link EcCommandSpecification} Object
     */
    public static EcCommandSpecification createCommandSpecification(final CommandType type, final String id,
            final Map<String, Object> config) {
        EcCommandSpecification spec = null;
        if (CommandType.TEST_AVP.equals(type)) {
            spec =
                    new TestAVPCommandSpecification("avpsessionId_" + id, "avppageId_" + id, "avpcommandrole_" + id,
                            "avpcommandtype_" + id, LOCALE_UK);
        } /*
           * else if (CommandType.PRICING.equals(type)) { String productNo = (String) config.get("productNo"); String accountNo = (String)
           * config.get("accountNo"); ProductService productService = ProductServiceLocator.getInstance().locate(LOCALE_UK); Product product
           * = productService.getProduct(new ProductNumber(productNo), LOCALE_UK); AccountService accountService =
           * AccountServiceLocator.getLocator().locate(LOCALE_UK); List<CustomerAccount> accountList = accountService.getAccounts(accountNo,
           * LOCALE_UK); CustomerAccount customerAccount = accountList.get(0); spec = new PricingCommandSpecification("prc_sessionId_" + id,
           * "prc_pageId_" + id, "prc_commandRole_" + id, LOCALE_UK, product, customerAccount); } else if
           * (CommandType.AVAILABILITY.equals(type)) { String productNo = (String) config.get("productNo"); String accountNo = (String)
           * config.get("accountNo"); Plant basePlant = getPlantForSite(LOCALE_UK.getLocaleString()); Quantity quantity = null;
           * ProductService productService = ProductServiceLocator.getInstance().locate(LOCALE_UK); Product product =
           * productService.getProduct(new ProductNumber(productNo), LOCALE_UK); AccountService accountService =
           * AccountServiceLocator.getLocator().locate(LOCALE_UK); List<CustomerAccount> accountList = accountService.getAccounts(accountNo,
           * LOCALE_UK); CustomerAccount customerAccount = accountList.get(0); // Quantity AvailabilityService availabilityService =
           * AvailabilityServiceLocator.getInstance().locate(LOCALE_UK); Availability availability =
           * availabilityService.checkAvailability(product, customerAccount, LOCALE_UK, basePlant); quantity =
           * availability.getQuantityAvailable(); AvailabilityCommandSpecification availSpec = new
           * AvailabilityCommandSpecification("avail_sessionId_" + id, "avail_pageId_" + id, "avail_commandRole_" + id, LOCALE_UK);
           * availSpec.setProduct(product); availSpec.setQuantity(quantity); availSpec.setSoldTo(customerAccount);
           * availSpec.setBasePlant(basePlant); spec = availSpec; }
           */
        return spec;
    }

    /**
     * This method returns a plant for given Site string.
     * @param siteString as a siteString.
     * @return plant.
     */
//NGSPFO-194 - Removed as no longer needed
//    @SuppressWarnings("unchecked")
//    private static Plant getPlantForSite(final String siteString) {
//        Site site = new Site(siteString);
//        Plant plant = null;
//        PlantServiceLocator plantServiceLocator = PlantServiceLocator.getLocator();
//        PlantService plantService = plantServiceLocator.locate(new Locale(siteString));
//        assertNotNull("Plant service returned is equal to null.", plantService);
//        List<Plant> plants = plantService.getPlants(site);
//        if (plants != null && plants.size() > 0) {
//            plant = plants.get(0);
//        }
//        return plant;
//    }
}
