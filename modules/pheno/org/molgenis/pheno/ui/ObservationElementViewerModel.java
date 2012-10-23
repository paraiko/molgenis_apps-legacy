package org.molgenis.pheno.ui;

import java.util.List;

import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.ui.EasyPluginModel;
import org.molgenis.framework.ui.html.Input;
import org.molgenis.pheno.ObservedValue;
import org.molgenis.pheno.dto.ObservationElementDTO;
import org.molgenis.pheno.dto.ProtocolDTO;
import org.molgenis.pheno.ui.form.ApplyProtocolForm;
import org.molgenis.pheno.ui.form.SelectProtocolForm;
import org.molgenis.pheno.ui.form.ObservationTargetForm;

public class ObservationElementViewerModel extends EasyPluginModel
{
	private static final long serialVersionUID = 2545649822397395528L;
	private String action;
	private Integer id;
	private ObservationElementDTO observationElementDTO;
	private ObservationTargetForm observationTargetForm;
	private ProtocolDTO protocolDTO;
	private SelectProtocolForm selectProtocolForm;
	private List<ProtocolDTO> protocolDTOList;
	private ApplyProtocolForm applyProtocolForm;

	public ObservationElementViewerModel(ObservationElementViewer controller)
	{
		super(controller);
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public ObservationElementDTO getObservationElementDTO()
	{
		return observationElementDTO;
	}

	public void setObservationElementDTO(ObservationElementDTO observationElementDTO)
	{
		this.observationElementDTO = observationElementDTO;
	}

	public ObservationTargetForm getObservationTargetForm()
	{
		return observationTargetForm;
	}

	public void setObservationTargetForm(ObservationTargetForm observationTargetForm)
	{
		this.observationTargetForm = observationTargetForm;
	}

	public ProtocolDTO getProtocolDTO()
	{
		return protocolDTO;
	}

	public void setProtocolDTO(ProtocolDTO protocolDTO)
	{
		this.protocolDTO = protocolDTO;
	}

	public SelectProtocolForm getSelectProtocolForm()
	{
		return selectProtocolForm;
	}

	public void setSelectProtocolForm(SelectProtocolForm protocolForm)
	{
		this.selectProtocolForm = protocolForm;
	}

	public List<ProtocolDTO> getProtocolDTOList()
	{
		return protocolDTOList;
	}

	public void setProtocolDTOList(List<ProtocolDTO> protocolDTOList)
	{
		this.protocolDTOList = protocolDTOList;
	}

	public ApplyProtocolForm getApplyProtocolForm()
	{
		return applyProtocolForm;
	}

	public void setApplyProtocolForm(ApplyProtocolForm applyProtocolForm)
	{
		this.applyProtocolForm = applyProtocolForm;
	}

	public Input<?> createIndividualInput(String name)
	{
		return this.observationTargetForm.get(name);
	}

	public Input<?> createProtocolInput(String name)
	{
		return this.applyProtocolForm.get(name);
	}

	public boolean isEditable()
	{
		try
		{
			return this.getController().getApplicationController().getLogin().canWrite(ObservedValue.class);
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}